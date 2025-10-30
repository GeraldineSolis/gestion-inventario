package com.application.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.application.ui.theme.DesignTokens
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CierreScreen() {
    val currency = NumberFormat.getCurrencyInstance(Locale("es", "MX"))
    val fecha = SimpleDateFormat("EEEE, d 'De' MMMM 'De' yyyy", Locale("es", "ES")).format(Date())

    // Datos simulados
    val ventasTotales = 12450.75
    val gastosTotales = 3280.50
    val utilidad = ventasTotales - gastosTotales
    val margen = if (ventasTotales > 0) (utilidad / ventasTotales) * 100 else 0.0

    val historial = listOf(
        CierreItem("Mié, 22 De Oct De 2025", 11230.50, 2890.00, 8340.50),
        CierreItem("Mar, 21 De Oct De 2025", 9875.25, 3120.75, 6754.50),
        CierreItem("Lun, 20 De Oct De 2025", 13456.00, 4200.00, 9256.00)
    )

    Surface(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(DesignTokens.AppBackground)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Column {
                    Text("Cierre de Caja", style = MaterialTheme.typography.titleLarge, fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
                    Text(fecha.replaceFirstChar { it.uppercase() }, color = DesignTokens.TextSecondary)
                }
            }

            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = DesignTokens.CardElevation),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(Modifier.fillMaxWidth()) {
                        // Encabezado verde
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFF2E7D32))
                                .padding(horizontal = 16.dp, vertical = 12.dp)
                        ) {
                            Column {
                                Text("Resumen Financiero del Día", color = Color.White, fontWeight = FontWeight.SemiBold)
                                Text("Cálculo de utilidad diaria", color = Color.White.copy(alpha = 0.85f), fontSize = 12.sp)
                            }
                        }

                        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            // Ventas
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFFE8F5E9), RoundedCornerShape(8.dp))
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("Ventas Totales", fontWeight = FontWeight.Medium)
                                    Text("23 transacciones", color = DesignTokens.TextMuted, fontSize = 12.sp)
                                }
                                Text(currency.format(ventasTotales), color = Color(0xFF2E7D32), fontWeight = FontWeight.SemiBold)
                            }

                            // Gastos
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFFFFEBEE), RoundedCornerShape(8.dp))
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("Gastos Totales", fontWeight = FontWeight.Medium)
                                    Text("5 movimientos", color = DesignTokens.TextMuted, fontSize = 12.sp)
                                }
                                Text("-${currency.format(gastosTotales).replace("$", "")}", color = Color(0xFFD32F2F), fontWeight = FontWeight.SemiBold)
                            }

                            // Utilidad
                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF1976D2)),
                                elevation = CardDefaults.cardElevation(defaultElevation = DesignTokens.CardElevation),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Box(Modifier.fillMaxWidth().padding(16.dp)) {
                                    Column(Modifier.fillMaxWidth()) {
                                        Text("Utilidad del Día", color = Color.White.copy(alpha = 0.9f))
                                        Text("Ventas - Gastos", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                                        Spacer(Modifier.height(8.dp))
                                        Text(currency.format(utilidad), color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                                    }
                                    AssistChip(
                                        onClick = {},
                                        label = { Text("${"%.1f".format(margen)}% margen") },
                                        modifier = Modifier.align(Alignment.TopEnd),
                                        colors = AssistChipDefaults.assistChipColors(containerColor = Color.White.copy(alpha = 0.2f), labelColor = Color.White)
                                    )
                                }
                            }

                            Button(
                                onClick = { /* TODO cerrar caja */ },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("Cerrar Caja del Día")
                            }
                        }
                    }
                }
            }

            item {
                Text("Historial de Cierres", fontWeight = FontWeight.SemiBold, color = DesignTokens.TextSecondary)
            }

            items(historial) { item ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = DesignTokens.CardElevation),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Description, contentDescription = null, tint = DesignTokens.BluePrimary)
                            Spacer(Modifier.width(8.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(item.fecha, fontWeight = FontWeight.Medium)
                                    Spacer(Modifier.width(8.dp))
                                    AssistChip(onClick = {}, label = { Text("Cerrado") })
                                }
                                Spacer(Modifier.height(6.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                    Column { Text("Ventas", color = DesignTokens.TextMuted, fontSize = 12.sp); Text(currency.format(item.ventas)) }
                                    Column { Text("Gastos", color = DesignTokens.TextMuted, fontSize = 12.sp); Text(currency.format(item.gastos), color = Color(0xFFD32F2F)) }
                                    Column { Text("Utilidad", color = DesignTokens.TextMuted, fontSize = 12.sp); Text(currency.format(item.utilidad), color = DesignTokens.BluePrimary, fontWeight = FontWeight.SemiBold) }
                                }
                            }
                        }
                        OutlinedButton(onClick = { /* TODO detalle */ }) { Text("Ver Detalle") }
                    }
                }
            }
        }
    }
}

private data class CierreItem(
    val fecha: String,
    val ventas: Double,
    val gastos: Double,
    val utilidad: Double
)