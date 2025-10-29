package com.application.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.application.data.repository.Resource
import com.application.model.Producto
import com.application.navigation.Screens
import com.application.viewmodel.HomeViewModel
import java.text.NumberFormat
import android.app.Application
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider

@Composable
fun HomeScreen(navController: NavHostController) {
    val application = LocalContext.current.applicationContext as Application
    val vm: HomeViewModel = viewModel(factory = ViewModelProvider.AndroidViewModelFactory.getInstance(application))

    val statsResource by vm.dashboardStats.collectAsState()
    val lowStock by vm.productosBajoStock.collectAsState()
    val totalHoy by vm.totalVendidoHoy.collectAsState()
    val cantidadHoy by vm.cantidadVendidaHoy.collectAsState()

    val currency = NumberFormat.getCurrencyInstance()

    Surface(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F7FA))
                .padding(horizontal = 12.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = "Dashboard",
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Resumen de ventas y estado del inventario",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF6B7280)
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    DashboardCard(
                        title = "Ventas del Día",
                        subtitle = cantidadHoy?.let { "$it productos vendidos" },
                        value = totalHoy?.let { currency.format(it) } ?: "...",
                        icon = Icons.Filled.AttachMoney,
                        color = Color(0xFF1E88E5),
                        modifier = Modifier.weight(1f)
                    )

                    DashboardCard(
                        title = "Alertas de Stock",
                        subtitle = "productos con bajo stock",
                        value = when (val s = statsResource) {
                            is Resource.Success -> (s.data?.productosBajoStock ?: 0).toString()
                            is Resource.Error -> "—"
                            is Resource.Loading -> "..."
                        },
                        icon = Icons.Filled.WarningAmber,
                        color = Color(0xFFF57C00),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                LowStockSection(lowStock)
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ActionCard(
                        title = "Nueva Venta",
                        subtitle = "Registrar venta rápida",
                        icon = Icons.Filled.ShoppingCart,
                        onClick = { navController.navigate(Screens.Ventas.route) },
                        modifier = Modifier.weight(1f)
                    )
                    ActionCard(
                        title = "Agregar Producto",
                        subtitle = "Registrar nuevo producto",
                        icon = Icons.Filled.AddCircle,
                        onClick = { navController.navigate(Screens.ProductEntry.route) },
                        modifier = Modifier.weight(1f)
                    )
                    ActionCard(
                        title = "Ver Reportes",
                        subtitle = "Análisis y estadísticas",
                        icon = Icons.Filled.Analytics,
                        onClick = { navController.navigate(Screens.Cierre.route) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun DashboardCard(
    title: String,
    subtitle: String?,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .heightIn(min = 120.dp),
        colors = CardDefaults.cardColors(containerColor = color),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = title, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                Icon(imageVector = icon, contentDescription = null, tint = Color.White.copy(alpha = 0.9f))
            }
            Spacer(Modifier.height(12.dp))
            Text(text = value, color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            if (subtitle != null) {
                Spacer(Modifier.height(2.dp))
                Text(text = subtitle, color = Color.White.copy(alpha = 0.85f), fontSize = 12.sp)
            }
        }
    }
}

@Composable
private fun LowStockSection(itemsLow: List<Producto>) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF7E6)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Filled.WarningAmber, contentDescription = null, tint = Color(0xFFF57C00))
                Spacer(Modifier.width(8.dp))
                Text("Productos que Requieren Reabastecimiento", color = Color(0xFF8A6D3B), fontWeight = FontWeight.Medium)
            }
            Divider(color = Color(0xFFE8E8E8))
            if (itemsLow.isEmpty()) {
                Text(
                    text = "Sin alertas de stock",
                    modifier = Modifier.padding(16.dp),
                    color = Color(0xFF6B7280)
                )
            } else {
                itemsLow.forEach { producto ->
                    LowStockRow(producto)
                    Divider(color = Color(0xFFE8E8E8))
                }
            }
        }
    }
}

@Composable
private fun LowStockRow(producto: Producto) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(Modifier.weight(1f)) {
            Text(producto.nombre, fontWeight = FontWeight.SemiBold)
            Text("Stock mínimo: ${producto.stockMinimo} unidades", color = Color(0xFF6B7280), fontSize = 12.sp)
        }
        Column(horizontalAlignment = Alignment.End) {
            Text("Stock actual", color = Color(0xFF6B7280), fontSize = 12.sp)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("${producto.stockActual}")
                Spacer(Modifier.width(8.dp))
                AssistChip(
                    onClick = {},
                    label = { Text("Bajo") },
                    colors = AssistChipDefaults.assistChipColors(containerColor = Color(0xFFFFE4D6))
                )
            }
        }
    }
}

@Composable
private fun ActionCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.height(110.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = null, tint = Color(0xFF1E88E5))
            Spacer(Modifier.height(8.dp))
            Text(title, fontWeight = FontWeight.SemiBold)
            Text(subtitle, color = Color(0xFF6B7280), fontSize = 12.sp)
        }
    }
}