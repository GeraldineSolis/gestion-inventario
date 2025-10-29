package com.application.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.application.data.AppDatabase
import com.application.data.repository.InventoryRepository
import com.application.data.repository.Resource
import com.application.model.Venta
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CartItem(
    val id: Int,
    val nombre: String,
    val precio: Double,
    val stockActual: Int,
    val cantidad: Int
)

class VentasViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: InventoryRepository

    private val _cart = MutableStateFlow<List<CartItem>>(emptyList())
    val cart: StateFlow<List<CartItem>> = _cart.asStateFlow()

    private val _isProcessing = MutableStateFlow(false)
    val isProcessing: StateFlow<Boolean> = _isProcessing.asStateFlow()

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()

    init {
        val db = AppDatabase.getDatabase(application)
        repository = InventoryRepository(
            productoDao = db.productoDao(),
            compraDao = db.compraDao(),
            ventaDao = db.ventaDao()
        )
    }

    fun addOrIncrement(id: Int, nombre: String, precio: Double, stockActual: Int) {
        val items = _cart.value.toMutableList()
        val idx = items.indexOfFirst { it.id == id }
        if (idx >= 0) {
            val item = items[idx]
            if (item.cantidad < stockActual) {
                items[idx] = item.copy(cantidad = item.cantidad + 1)
            }
        } else {
            if (stockActual > 0) {
                items.add(CartItem(id, nombre, precio, stockActual, 1))
            }
        }
        _cart.value = items
    }

    fun decrement(id: Int) {
        val items = _cart.value.toMutableList()
        val idx = items.indexOfFirst { it.id == id }
        if (idx >= 0) {
            val item = items[idx]
            val newQty = item.cantidad - 1
            if (newQty <= 0) items.removeAt(idx) else items[idx] = item.copy(cantidad = newQty)
            _cart.value = items
        }
    }

    fun remove(id: Int) {
        _cart.value = _cart.value.filterNot { it.id == id }
    }

    fun clear() {
        _cart.value = emptyList()
    }

    fun clearMessage() { _message.value = null }

    fun checkout() {
        if (_cart.value.isEmpty() || _isProcessing.value) return
        viewModelScope.launch {
            _isProcessing.value = true
            try {
                for (item in _cart.value) {
                    val venta = Venta(
                        id = 0,
                        productoId = item.id,
                        nombreProducto = item.nombre,
                        cantidad = item.cantidad,
                        precioUnitario = item.precio,
                        fechaVenta = System.currentTimeMillis()
                    )
                    when (val res = repository.insertVenta(venta)) {
                        is Resource.Success -> { /* continue */ }
                        is Resource.Error -> {
                            _message.value = res.message ?: "Error al registrar venta"
                            _isProcessing.value = false
                            return@launch
                        }
                        is Resource.Loading -> {}
                    }
                }
                clear()
                _message.value = "Venta registrada exitosamente"
            } catch (e: Exception) {
                _message.value = e.message ?: "Error inesperado"
            } finally {
                _isProcessing.value = false
            }
        }
    }
}
