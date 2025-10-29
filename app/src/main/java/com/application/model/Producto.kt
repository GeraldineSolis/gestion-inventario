package com.application.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "productos")
data class Producto(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val stockActual: Int,
    val stockMinimo: Int,
    val categoria: String,
    val fechaCreacion: Long = System.currentTimeMillis()
)
