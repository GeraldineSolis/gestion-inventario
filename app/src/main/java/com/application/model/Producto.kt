package com.application.model

data class Producto(
    val id: Int = 0,
    val nombre: String,
    val precioVenta: Double,
    val costo: Double,
    val stockActual: Int,
    val sku: String? = null
)
