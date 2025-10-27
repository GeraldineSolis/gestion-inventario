package com.application.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.application.data.AppDatabase
import com.application.data.repository.DashboardStats
import com.application.data.repository.InventoryRepository
import com.application.data.repository.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: InventoryRepository

    private val _dashboardStats = MutableStateFlow<Resource<DashboardStats>>(Resource.Loading())
    val dashboardStats: StateFlow<Resource<DashboardStats>> = _dashboardStats.asStateFlow()

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
            repository.getEstadisticasDashboard().collect { stats ->
                _dashboardStats.value = stats
            }
        }
    }
}