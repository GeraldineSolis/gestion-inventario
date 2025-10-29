package com.application.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.Color


object NavItem {
    data class Item(
        val route: String,
        val label: String,
        val icon: ImageVector,
        val selectedColor: Color
    )

    val items = listOf(
        Item(Screens.Home.route, "Dashboard", Icons.Default.Home, Color(0xFF1E88E5)),
        Item(Screens.Ventas.route, "Ventas", Icons.Default.ShoppingCart, Color(0xFF2E7D32)),
        Item(Screens.Productos.route, "Productos", Icons.Default.ShoppingCart, Color(0xFF1E88E5)),
        Item(Screens.Gastos.route, "Gastos", Icons.Default.ShoppingCart, Color(0xFFF57C00)),
        Item(Screens.Cierre.route, "Cierre", Icons.Default.ShoppingCart, Color(0xFF1E88E5))
    )
}