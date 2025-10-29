package com.application.data.repository

data class DashboardStats(
    val totalProductos: Int,
    val productosBajoStock: Int,
    val costoTotalInventario: Double,
    val totalGastadoCompras: Double,
    val totalIngresosVentas: Double,
    val gananciaNeta: Double,
    val totalCompras: Int,
    val totalVentas: Int
)