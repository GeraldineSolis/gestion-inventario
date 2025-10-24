package com.application

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.application.navigation.Screens
import com.application.ui.CierreScreen
import com.application.ui.GastosScreen
import com.application.ui.HomeScreen
import com.application.ui.ProductScreen
import com.application.ui.VentasScreen
import com.application.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screens.Home.route
    ) {

        composable(Screens.Home.route) { HomeScreen(navController) }

        composable(Screens.Ventas.route) { VentasScreen() }
        composable(Screens.Productos.route) { ProductScreen() }
        composable(Screens.Gastos.route) { GastosScreen() }
        composable(Screens.Cierre.route) { CierreScreen() }
    }
}