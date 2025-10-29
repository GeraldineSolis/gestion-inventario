package com.application.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.application.navigation.NavItem
import com.application.navigation.Screens

@Composable
fun AppHeaderWithNav(navController: NavHostController, currentRoute: String) {
    Column(
        modifier = Modifier.fillMaxWidth().background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1E88E5)) // Color azul
                .padding(horizontal = 16.dp, vertical = 10.dp)
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

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(vertical = 4.dp, horizontal = 8.dp)
        ) {
            NavItem.items.forEach { item ->
                val isSelected = currentRoute == item.route

                OutlinedButton(
                    onClick = {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    modifier = Modifier.padding(horizontal = 4.dp),
                    colors = if (isSelected) ButtonDefaults.buttonColors(containerColor = Color(0xFF1E88E5))
                    else ButtonDefaults.outlinedButtonColors()
                ) {
                    Text(item.label, color = if (isSelected) Color.White else Color.Black)
                }
            }
        }
    }
}