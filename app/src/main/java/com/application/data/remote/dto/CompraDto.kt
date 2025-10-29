package com.application.data.remote.dto

import com.application.model.Compra
import com.google.gson.annotations.SerializedName

data class CompraDto(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("producto_id")
    val productoId: Int,
    @SerializedName("nombre_producto")
    val nombreProducto: String,
    @SerializedName("cantidad")
    val cantidad: Int,
    @SerializedName("precio_unitario")
    val precioUnitario: Double,
    @SerializedName("fecha_compra")
    val fechaCompra: Long = System.currentTimeMillis(),
)

fun CompraDto.toEntity(): Compra {
    return Compra(
        id = this.id,
        productoId = this.productoId,
        nombreProducto = this.nombreProducto,
        cantidad = this.cantidad,
        precioUnitario = this.precioUnitario,
        fechaCompra = this.fechaCompra,
    )
}

fun Compra.toDto(): CompraDto {
    return CompraDto(
        id = this.id,
        productoId = this.productoId,
        nombreProducto = this.nombreProducto,
        cantidad = this.cantidad,
        precioUnitario = this.precioUnitario,
        fechaCompra = this.fechaCompra,
    )
}