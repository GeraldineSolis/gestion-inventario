package com.application.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.application.data.AppDatabase
import com.application.data.repository.DashboardStats
import com.application.data.repository.InventoryRepository
import com.application.data.repository.Resource
import com.application.model.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: InventoryRepository

    private val _dashboardStats = MutableStateFlow<Resource<DashboardStats>>(Resource.Loading())
    val dashboardStats: StateFlow<Resource<DashboardStats>> = _dashboardStats.asStateFlow()

    private val _productosBajoStock = MutableStateFlow<List<Producto>>(emptyList())
    val productosBajoStock: StateFlow<List<Producto>> = _productosBajoStock.asStateFlow()

    private val _totalVendidoHoy = MutableStateFlow<Double?>(null)
    val totalVendidoHoy: StateFlow<Double?> = _totalVendidoHoy.asStateFlow()

    private val _cantidadVendidaHoy = MutableStateFlow<Int?>(null)
    val cantidadVendidaHoy: StateFlow<Int?> = _cantidadVendidaHoy.asStateFlow()

    init {
        val database = AppDatabase.getDatabase(application)
        repository = InventoryRepository(
            productoDao = database.productoDao(),
            compraDao = database.compraDao(),
            ventaDao = database.ventaDao()
        )
        loadDashboard()
        observeLowStock()
        observeVentasHoy()
    }

    private fun loadDashboard() {
        viewModelScope.launch {
            repository.getEstadisticasDashboard().collect { stats ->
                _dashboardStats.value = stats
            }
        }
    }

    private fun observeLowStock() {
        viewModelScope.launch {
            repository.getProductosBajoStock().collect { productos ->
                _productosBajoStock.value = productos
            }
        }
    }

    private fun observeVentasHoy() {
        viewModelScope.launch {
            val (inicio, fin) = getHoyInicioFin()
            repository.getTotalVendidoEnRango(inicio, fin).collect { total ->
                _totalVendidoHoy.value = total ?: 0.0
            }
        }
        viewModelScope.launch {
            val (inicio, fin) = getHoyInicioFin()
            repository.getCantidadVendidaEnRango(inicio, fin).collect { cantidad ->
                _cantidadVendidaHoy.value = cantidad ?: 0
            }
        }
    }

    private fun getHoyInicioFin(): Pair<Long, Long> {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        val inicio = cal.timeInMillis
        cal.set(Calendar.HOUR_OF_DAY, 23)
        cal.set(Calendar.MINUTE, 59)
        cal.set(Calendar.SECOND, 59)
        cal.set(Calendar.MILLISECOND, 999)
        val fin = cal.timeInMillis
        return inicio to fin
    }
}