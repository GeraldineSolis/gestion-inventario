package com.application.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.application.data.AppDatabase
import com.application.data.repository.InventoryRepository
import com.application.model.Compra
import com.application.model.Venta
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class CierreUiState(
    val fechaReporte: Date = Date(),
    val ventasTotalesDia: Double = 0.0,
    val transaccionesVentaDia: Int = 0,
    val gastosTotalesDia: Double = 0.0,
    val movimientosCompraDia: Int = 0,
    val utilidadDelDia: Double = 0.0,
    val margenUtilidad: Double = 0.0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val historialCierres: List<CierreDiarioHistorico> = emptyList(),
    val showCierreExitoso: Boolean = false
)

data class CierreDiarioHistorico(
    val id: Int = 0,
    val fechaCierre: Long,
    val ventas: Double,
    val gastos: Double,
    val utilidad: Double,
    val estado: String = "Cerrado"
)

class CierreViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: InventoryRepository

    private val _uiState = MutableStateFlow(CierreUiState())
    val uiState: StateFlow<CierreUiState> = _uiState.asStateFlow()

    val dateFormatter = SimpleDateFormat("EEEE, dd 'de' MMMM 'de' yyyy", Locale("es", "ES"))

    init {
        val database = AppDatabase.getDatabase(application)
        repository = InventoryRepository(
            productoDao = database.productoDao(),
            compraDao = database.compraDao(),
            ventaDao = database.ventaDao()
        )
        loadDailyStats()
        loadHistorialCierres()
    }

    private fun loadDailyStats() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val ventasFlow = repository.getVentasDelDia()
            val comprasFlow = repository.getComprasDelDia()

            combine(ventasFlow, comprasFlow) { ventas, compras ->
                val ventasTotales = ventas.sumOf { it.cantidad * it.precioUnitario }
                val transaccionesVenta = ventas.size

                val gastosTotales = compras.sumOf { it.cantidad * (it.precioUnitario ?: 0.0) }
                val movimientosCompra = compras.size

                val utilidad = ventasTotales - gastosTotales
                val margen = if (ventasTotales > 0) (utilidad / ventasTotales) * 100 else 0.0

                _uiState.value = _uiState.value.copy(
                    ventasTotalesDia = ventasTotales,
                    transaccionesVentaDia = transaccionesVenta,
                    gastosTotalesDia = gastosTotales,
                    movimientosCompraDia = movimientosCompra,
                    utilidadDelDia = utilidad,
                    margenUtilidad = margen,
                    isLoading = false,
                    errorMessage = null
                )
            }.collect {
            }
        }
    }

    private fun loadHistorialCierres() {
        val calendar = Calendar.getInstance()
        val dummyHistorial = listOf(
            CierreDiarioHistorico(
                id = 1,
                fechaCierre = calendar.apply { add(Calendar.DAY_OF_YEAR, -1); set(Calendar.HOUR_OF_DAY, 18) }.timeInMillis,
                ventas = 11230.50,
                gastos = 2890.00,
                utilidad = 8340.50
            ),
            CierreDiarioHistorico(
                id = 2,
                fechaCierre = calendar.apply { add(Calendar.DAY_OF_YEAR, -1); set(Calendar.HOUR_OF_DAY, 18) }.timeInMillis,
                ventas = 9500.00,
                gastos = 1500.00,
                utilidad = 8000.00
            )
        )
        _uiState.value = _uiState.value.copy(historialCierres = dummyHistorial.sortedByDescending { it.fechaCierre })
    }

    fun clearMessages() {
        _uiState.value = _uiState.value.copy(
            errorMessage = null,
            showCierreExitoso = false
        )
    }

    fun cerrarCajaDelDia() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            if (_uiState.value.ventasTotalesDia == 0.0 && _uiState.value.gastosTotalesDia == 0.0) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "No hay movimientos para cerrar la caja del día."
                )
                return@launch
            }

            try {
                val cierreHistorico = CierreDiarioHistorico(
                    id = _uiState.value.historialCierres.size + 1,
                    fechaCierre = System.currentTimeMillis(),
                    ventas = _uiState.value.ventasTotalesDia,
                    gastos = _uiState.value.gastosTotalesDia,
                    utilidad = _uiState.value.utilidadDelDia
                )

                val updatedHistorial = _uiState.value.historialCierres.toMutableList()
                updatedHistorial.add(0, cierreHistorico)

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    showCierreExitoso = true,
                    historialCierres = updatedHistorial.sortedByDescending { it.fechaCierre }
                )

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Error al cerrar caja: ${e.message}"
                )
            }
        }
    }
}
