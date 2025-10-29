package com.application.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector


object NavItem {
    data class Item(
        val route: String,
        val label: String,
        val icon: ImageVector
    )

    val items = listOf(
        Item(Screens.Home.route, "Dashboard", Icons.Default.Home),
        Item(Screens.Ventas.route, "Ventas", Icons.Default.ShoppingCart),
        Item(Screens.Productos.route, "Productos", Icons.Default.ShoppingCart),
        Item(Screens.Gastos.route, "Gastos", Icons.Default.ShoppingCart),
        Item(Screens.Cierre.route, "Cierre", Icons.Default.ShoppingCart)
    )
}