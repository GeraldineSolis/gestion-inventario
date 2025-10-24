package com.application.model

import java.util.Date

data class Compra(
    val id: Int = 0,
    val descripcion: String,
    val montoTotal: Double,
    val esGastoInsumo: Boolean = false,
    val fecha: Date = Date()
)
