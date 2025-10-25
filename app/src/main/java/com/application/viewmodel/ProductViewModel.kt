package com.application.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.application.data.AppDatabase
import com.application.data.InventoryRepository
import com.application.model.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ProductUiState(
    val productos: List<Producto> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val totalProductos: Int = 0,
    val productosBajoStock: Int = 0
)

class ProductViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: InventoryRepository

    private val _uiState = MutableStateFlow(ProductUiState())
    val uiState: StateFlow<ProductUiState> = _uiState.asStateFlow()

    init {
        val database = AppDatabase.getDatabase(application)
        repository = InventoryRepository(
            productoDao = database.productoDao(),
            compraDao = database.compraDao()
        )
        loadProductos()
    }

    private fun loadProductos() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                repository.getAllProductos().collect { productos ->
                    val bajoStock = productos.count { it.stockActual <= it.stockMinimo }
                    _uiState.value = ProductUiState(
                        productos = productos,
                        isLoading = false,
                        totalProductos = productos.size,
                        productosBajoStock = bajoStock
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Error desconocido"
                )
            }
        }
    }

    fun insertProducto(producto: Producto) {
        viewModelScope.launch {
            try {
                repository.insertProducto(producto)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Error al insertar producto: ${e.message}"
                )
            }
        }
    }

    fun updateProducto(producto: Producto) {
        viewModelScope.launch {
            try {
                repository.updateProducto(producto)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Error al actualizar producto: ${e.message}"
                )
            }
        }
    }

    fun deleteProducto(producto: Producto) {
        viewModelScope.launch {
            try {
                repository.deleteProducto(producto)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Error al eliminar producto: ${e.message}"
                )
            }
        }
    }

    fun getProductosByCategoria(categoria: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                repository.getProductosByCategoria(categoria).collect { productos ->
                    _uiState.value = _uiState.value.copy(
                        productos = productos,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Error al filtrar por categoría"
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}