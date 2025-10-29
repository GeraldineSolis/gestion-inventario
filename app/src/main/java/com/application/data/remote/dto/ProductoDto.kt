package com.application.data.remote.dto

import com.application.model.Producto
import com.google.gson.annotations.SerializedName

data class ProductoDto(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("nombre")
    val nombre: String,
    @SerializedName("descripcion")
    val descripcion: String,
    @SerializedName("precio")
    val precio: Double,
    @SerializedName("stock_actual")
    val stockActual: Int,
    @SerializedName("stock_minimo")
    val stockMinimo: Int,
    @SerializedName("categoria")
    val categoria: String,
    @SerializedName("fecha_creacion")
    val fechaCreacion: Long = System.currentTimeMillis()
)

fun ProductoDto.toEntity(): Producto {
    return Producto(
        id = this.id,
        nombre = this.nombre,
        descripcion = this.descripcion,
        precio = this.precio,
        stockActual = this.stockActual,
        stockMinimo = this.stockMinimo,
        categoria = this.categoria,
        fechaCreacion = this.fechaCreacion
    )
}

fun Producto.toDto(): ProductoDto {
    return ProductoDto(
        id = this.id,
        nombre = this.nombre,
        descripcion = this.descripcion,
        precio = this.precio,
        stockActual = this.stockActual,
        stockMinimo = this.stockMinimo,
        categoria = this.categoria,
        fechaCreacion = this.fechaCreacion
    )
}