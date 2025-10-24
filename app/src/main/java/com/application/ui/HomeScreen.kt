package com.application.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.application.navigation.HorizontalNavigationBar

@Composable
fun HomeScreen(navController: NavHostController) {
    Column(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1E88E5)) // Color azul
                .padding(16.dp)
        ) {
            Text(
                text = "Sistema de Gestión de Inventario",
                fontSize = 20.sp,
                color = Color.White
            )
            Text(
                text = "Administra tu negocio de forma eficiente",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.7f)
            )
        }

        HorizontalNavigationBar(navController = navController)

        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            Text(
                text = "Dashboard",
                fontSize = 24.sp,
            )
            Text(
                text = "Resumen de ventas y estado del inventario",
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text("Tarjetas de Resumen...")
        }
    }
}