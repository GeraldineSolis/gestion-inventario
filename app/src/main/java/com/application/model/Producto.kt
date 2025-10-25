package com.application.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "productos")
data class Producto(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val precioVenta: Double,
    val costo: Double,
    val stockActual: Int,
    val stockMinimo: Int,
    val categoria: String,
    val sku: String? = null
)
