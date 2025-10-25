package com.application

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.application.navigation.Screens
import com.application.ui.AppHeaderWithNav
import com.application.ui.CierreScreen
import com.application.ui.GastosScreen
import com.application.ui.HomeScreen
import com.application.ui.ProductEntryScreen
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

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Screens.Home.route

    Scaffold(
        topBar = { AppHeaderWithNav(navController = navController, currentRoute = currentRoute) }
    ) { paddingValues ->

        NavHost(
            navController = navController,
            startDestination = Screens.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {

            composable(Screens.Home.route) { HomeScreen() }
            composable(Screens.Ventas.route) { VentasScreen() }
            composable(Screens.Productos.route) { ProductosScreen() }
            composable(Screens.Gastos.route) { GastosScreen() }
            composable(Screens.Cierre.route) { CierreScreen() }

            // Rutas Secundarias
            composable(Screens.ProductEntry.route) { ProductEntryScreen() }
        }
    }
}

@Composable
fun ProductosScreen() {
    TODO("Not yet implemented")
}