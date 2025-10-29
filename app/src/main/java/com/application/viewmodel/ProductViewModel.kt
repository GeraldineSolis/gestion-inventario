package com.application.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.application.data.AppDatabase
import com.application.data.repository.InventoryRepository
import com.application.data.repository.Resource
import com.application.model.Producto
import com.application.model.Venta
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CarritoItem(
    val producto: Producto,
    var cantidad: Int = 1
)

data class ProductUiState(
    val productos: List<Producto> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val totalProductos: Int = 0,
    val productosBajoStock: Int = 0,
    val isSyncing: Boolean = false,
    val syncMessage: String? = null
)

class ProductViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: InventoryRepository

    private val _uiState = MutableStateFlow(ProductUiState())
    val uiState: StateFlow<ProductUiState> = _uiState.asStateFlow()

    init {
        val database = AppDatabase.getDatabase(application)
        repository = InventoryRepository(
            productoDao = database.productoDao(),
            compraDao = database.compraDao(),
            ventaDao = database.ventaDao()
        )
        loadProductos()
    }

    private fun loadProductos() {
        viewModelScope.launch {
            repository.getAllProductos().collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = true)
                    }
                    is Resource.Success -> {
                        val productos = resource.data ?: emptyList()
                        val bajoStock = productos.count { it.stockActual <= it.stockMinimo }
                        _uiState.value = ProductUiState(
                            productos = productos,
                            isLoading = false,
                            totalProductos = productos.size,
                            productosBajoStock = bajoStock
                        )
                    }
                    is Resource.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = resource.message
                        )
                    }
                }
            }
        }
    }

    fun processSale(items: List<CarritoItem>) {
        if (items.isEmpty()) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSyncing = true, syncMessage = "Procesando venta...")

            try {
                val transactionId = System.currentTimeMillis()

                items.forEach { cartItem ->
                    val product = cartItem.producto
                    val quantitySold = cartItem.cantidad
                    val newStock = product.stockActual - quantitySold

                    if (newStock < 0) {
                        throw IllegalStateException("Stock insuficiente para ${product.nombre}. Stock actual: ${product.stockActual}, a vender: $quantitySold")
                    }

                    val updatedProduct = product.copy(stockActual = newStock)

                    when (val updateResult = repository.updateProducto(updatedProduct)) {
                        is Resource.Success -> { /* Stock actualizado */ }
                        is Resource.Error -> { throw Exception("Fallo al actualizar stock de ${product.nombre}: ${updateResult.message}") }
                        is Resource.Loading -> { /* Ignorar */ }
                    }

                    val venta = Venta(
                        productoId = product.id,
                        nombreProducto = product.nombre,
                        cantidad = quantitySold,
                        precioUnitario = product.precio ?: 0.0,
                        fechaVenta = transactionId,
                        cliente = "Cliente General"
                        // id = 0, se autogenera
                    )

                    when (val insertResult = repository.insertVenta(venta)) {
                        is Resource.Success -> { /* Venta registrada */ }
                        is Resource.Error -> { throw Exception("Fallo al registrar venta de ${product.nombre}: ${insertResult.message}") }
                        is Resource.Loading -> { /* Ignorar */ }
                    }
                }

                _uiState.value = _uiState.value.copy(
                    isSyncing = false,
                    syncMessage = "✅ Venta procesada exitosamente y stock actualizado.",
                )

                loadProductos()

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isSyncing = false,
                    errorMessage = "Error al procesar venta: ${e.message}"
                )
            }
        }
    }


    fun insertProducto(producto: Producto) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null, successMessage = null)

            when (val result = repository.insertProducto(producto)) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        successMessage = "✅ Producto registrado exitosamente"
                    )
                }
                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message
                    )
                }
                is Resource.Loading -> {}
            }
        }
    }

    fun clearMessages() {
        _uiState.value = _uiState.value.copy(
            errorMessage = null,
            successMessage = null
        )
    }

    fun updateProducto(producto: Producto) {
        viewModelScope.launch {
            when (val result = repository.updateProducto(producto)) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        syncMessage = "Producto actualizado exitosamente"
                    )
                }
                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = result.message
                    )
                }
                is Resource.Loading -> {}
            }
        }
    }

    fun deleteProducto(producto: Producto) {
        viewModelScope.launch {
            when (val result = repository.deleteProducto(producto)) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        syncMessage = "Producto eliminado exitosamente"
                    )
                }
                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = result.message
                    )
                }
                is Resource.Loading -> {}
            }
        }
    }

    fun syncWithServer() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSyncing = true)
            when (val result = repository.syncProductosFromServer()) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isSyncing = false,
                        syncMessage = "Sincronización completada"
                    )
                }
                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isSyncing = false,
                        errorMessage = result.message
                    )
                }
                is Resource.Loading -> {}
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    fun clearSyncMessage() {
        _uiState.value = _uiState.value.copy(syncMessage = null)
    }
}
