package com.application.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.application.data.AppDatabase
import com.application.data.repository.InventoryRepository
import com.application.data.repository.Resource
import com.application.model.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*

data class HomeUiState(
    val ventasDelDia: Double = 0.0,
    val productosVendidosHoy: Int = 0,
    val productosBajoStock: Int = 0,
    val productosBajoStockList: List<Producto> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: InventoryRepository

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        val database = AppDatabase.getDatabase(application)
        repository = InventoryRepository(
            productoDao = database.productoDao(),
            compraDao = database.compraDao(),
            ventaDao = database.ventaDao()
        )
        loadDashboard()
    }

    private fun loadDashboard() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            // Sincronizar productos desde Mock API
            repository.syncProductosFromServer()

            // Cargar productos con bajo stock
            launch {
                repository.getProductosBajoStock().collect { productos ->
                    _uiState.value = _uiState.value.copy(
                        productosBajoStock = productos.size,
                        productosBajoStockList = productos.sortedBy { it.stockActual }
                    )
                }
            }

            // Cargar ventas del día
            launch {
                repository.getVentasDelDia().collect { ventas ->
                    val totalVentas = ventas.sumOf { it.cantidad * it.precioUnitario }
                    val totalProductos = ventas.sumOf { it.cantidad }

                    _uiState.value = _uiState.value.copy(
                        ventasDelDia = totalVentas,
                        productosVendidosHoy = totalProductos
                    )
                }
            }

            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }

    fun formatPrice(price: Double): String {
        return "$${String.format("%,.2f", price)}"
    }

    fun refresh() {
        loadDashboard()
    }
}