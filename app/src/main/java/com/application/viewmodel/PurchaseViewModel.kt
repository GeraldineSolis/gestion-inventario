package com.application.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.application.data.AppDatabase
import com.application.data.repository.InventoryRepository
import com.application.data.repository.Resource
import com.application.model.Compra
import com.application.model.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

data class PurchaseUiState(
    val compras: List<Compra> = emptyList(),
    val productos: List<Producto> = emptyList(),
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val totalCompras: Int = 0,
    val totalGastado: Double = 0.0
)

class PurchaseViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: InventoryRepository

    private val _uiState = MutableStateFlow(PurchaseUiState())
    val uiState: StateFlow<PurchaseUiState> = _uiState.asStateFlow()

    init {
        val database = AppDatabase.getDatabase(application)
        repository = InventoryRepository(
            productoDao = database.productoDao(),
            compraDao = database.compraDao(),
            ventaDao = database.ventaDao()
        )
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            // Cargar compras
            launch {
                repository.getAllCompras().collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            _uiState.value = _uiState.value.copy(
                                compras = resource.data ?: emptyList(),
                                totalCompras = resource.data?.size ?: 0
                            )
                        }
                        is Resource.Error -> {
                            _uiState.value = _uiState.value.copy(
                                errorMessage = resource.message
                            )
                        }
                        is Resource.Loading -> {}
                    }
                }
            }

            // Cargar productos (para el selector)
            launch {
                repository.getAllProductos().collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            _uiState.value = _uiState.value.copy(
                                productos = resource.data ?: emptyList()
                            )
                        }
                        is Resource.Error -> {}
                        is Resource.Loading -> {}
                    }
                }
            }

            // Cargar total gastado
            launch {
                repository.getTotalGastado().collect { total ->
                    _uiState.value = _uiState.value.copy(
                        totalGastado = total ?: 0.0
                    )
                }
            }

            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }

    /**
     * Guarda una nueva compra y actualiza el stock automáticamente
     */
    fun saveCompra(compra: Compra) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSaving = true, errorMessage = null)

            when (val result = repository.insertCompra(compra)) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        successMessage = "✅ Compra registrada exitosamente. Stock actualizado."
                    )
                }
                is Resource.Error -> {
                    // Aunque haya error de red, si data != null significa que se guardó localmente
                    if (result.data != null) {
                        _uiState.value = _uiState.value.copy(
                            isSaving = false,
                            successMessage = "⚠️ ${result.message}",
                            errorMessage = null
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            isSaving = false,
                            errorMessage = result.message
                        )
                    }
                }
                is Resource.Loading -> {}
            }
        }
    }

    /**
     * Actualiza una compra existente
     */
    fun updateCompra(compra: Compra) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSaving = true)

            when (val result = repository.updateCompra(compra)) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        successMessage = "Compra actualizada exitosamente"
                    )
                }
                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        successMessage = if (result.message?.contains("localmente") == true) {
                            "⚠️ ${result.message}"
                        } else null,
                        errorMessage = if (result.message?.contains("localmente") == true) null else result.message
                    )
                }
                is Resource.Loading -> {}
            }
        }
    }

    /**
     * Elimina una compra
     */
    fun deleteCompra(compra: Compra) {
        viewModelScope.launch {
            when (val result = repository.deleteCompra(compra)) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        successMessage = "Compra eliminada exitosamente"
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

    /**
     * Filtra compras por producto
     */
    fun getComprasByProducto(productoId: Int) {
        viewModelScope.launch {
            repository.getComprasByProducto(productoId).collect { compras ->
                _uiState.value = _uiState.value.copy(
                    compras = compras
                )
            }
        }
    }

    /**
     * Filtra compras por rango de fechas
     */
    fun getComprasByRangoFechas(fechaInicio: Long, fechaFin: Long) {
        viewModelScope.launch {
            repository.getComprasByRangoFechas(fechaInicio, fechaFin).collect { compras ->
                _uiState.value = _uiState.value.copy(
                    compras = compras
                )
            }
        }
    }

    /**
     * Sincroniza con el servidor
     */
    fun syncWithServer() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            when (val result = repository.syncComprasFromServer()) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        successMessage = "✅ Sincronización completada"
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

    /**
     * Recarga todos los datos
     */
    fun refresh() {
        loadData()
    }

    /**
     * Limpia mensajes de error y éxito
     */
    fun clearMessages() {
        _uiState.value = _uiState.value.copy(
            errorMessage = null,
            successMessage = null
        )
    }

    /**
     * Formatea fecha para mostrar en la UI
     */
    fun formatDate(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    /**
     * Formatea fecha corta (solo día)
     */
    fun formatDateShort(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    /**
     * Formatea precio con símbolo de moneda
     */
    fun formatPrice(price: Double): String {
        return String.format("$%.2f", price)
    }

    /**
     * Calcula el total de una compra (cantidad × precio)
     */
    fun calculateTotal(cantidad: Int, precioUnitario: Double): Double {
        return cantidad * precioUnitario
    }

    /**
     * Valida que los campos de una compra sean correctos
     */
    fun validateCompra(
        productoId: Int?,
        cantidad: String,
        precioUnitario: String,
        proveedor: String
    ): Pair<Boolean, String> {

        if (productoId == null || productoId == 0) {
            return Pair(false, "Debe seleccionar un producto")
        }

        val cantidadInt = cantidad.toIntOrNull()
        if (cantidadInt == null || cantidadInt <= 0) {
            return Pair(false, "La cantidad debe ser un número mayor a 0")
        }

        val precioDouble = precioUnitario.toDoubleOrNull()
        if (precioDouble == null || precioDouble <= 0) {
            return Pair(false, "El precio debe ser un número mayor a 0")
        }

        if (proveedor.isBlank()) {
            return Pair(false, "Debe ingresar el nombre del proveedor")
        }

        return Pair(true, "")
    }

    /**
     * Obtiene las compras más recientes (últimas N)
     */
    fun getComprasRecientes(limit: Int = 5): List<Compra> {
        return _uiState.value.compras
            .sortedByDescending { it.fechaCompra }
            .take(limit)
    }

    /**
     * Obtiene compras del mes actual
     */
    fun getComprasMesActual(): List<Compra> {
        val calendar = Calendar.getInstance()
        val inicioMes = calendar.apply {
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        val finMes = calendar.apply {
            set(Calendar.DAY_OF_MONTH, getActualMaximum(Calendar.DAY_OF_MONTH))
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }.timeInMillis

        return _uiState.value.compras.filter {
            it.fechaCompra in inicioMes..finMes
        }
    }

    /**
     * Calcula el gasto total del mes actual
     */
    fun getTotalGastadoMesActual(): Double {
        return getComprasMesActual()
            .sumOf { it.cantidad * it.precioUnitario }
    }
}