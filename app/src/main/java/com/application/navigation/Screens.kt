package com.application.navigation

sealed class Screens(val route: String) {
    // 1. Dashboard
    object Home : Screens("dashboard")

    // 2. Ventas (Diseñador UI)
    object Ventas : Screens("ventas")

    // 3. Productos/Inventario
    object Productos : Screens("productos")
    object ProductEntry : Screens("productos/registro") // Formulario anidado

    // 4. Gastos
    object Gastos : Screens("gastos")

    // 5. Cierre/Reportes (Diseñador UI)
    object Cierre : Screens("cierre")
}