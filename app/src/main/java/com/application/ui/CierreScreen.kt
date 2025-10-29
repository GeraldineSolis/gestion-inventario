package com.application.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.application.viewmodel.CierreUiState
import com.application.viewmodel.CierreViewModel
import com.application.viewmodel.CierreDiarioHistorico
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Date
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CierreScreen(
    viewModel: CierreViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()


    val currencyFormatter = remember { NumberFormat.getCurrencyInstance(Locale("es", "MX")) }
    val historialDateFormatter = remember { SimpleDateFormat("MMM, dd 'de' yyyy", Locale("es", "ES")) }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            scope.launch {
                snackbarHostState.showSnackbar(message = message, withDismissAction = true)
                viewModel.clearMessages()
            }
        }
    }
    LaunchedEffect(uiState.showCierreExitoso) {
        if (uiState.showCierreExitoso) {
            scope.launch {
                snackbarHostState.showSnackbar(message = "✅ Caja del día cerrada exitosamente!", withDismissAction = true)
                viewModel.clearMessages()
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF1976D2))
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Cierre de Caja",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = viewModel.dateFormatter.format(uiState.fechaReporte),
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 14.sp
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(paddingValues)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Resumen Financiero del Día",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF333333),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Cálculo de utilidad diaria",
                            color = Color.Gray,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        FinancialSummaryRow(
                            icon = Icons.Default.TrendingUp,
                            iconTint = Color(0xFF4CAF50),
                            label = "Ventas Totales",
                            value = currencyFormatter.format(uiState.ventasTotalesDia),
                            transactions = "${uiState.transaccionesVentaDia} transacciones"
                        )
                        Divider(modifier = Modifier.padding(vertical = 8.dp))


                        FinancialSummaryRow(
                            icon = Icons.Default.TrendingDown,
                            iconTint = Color(0xFFD32F2F),
                            label = "Gastos Totales",
                            value = currencyFormatter.format(uiState.gastosTotalesDia),
                            transactions = "${uiState.movimientosCompraDia} movimientos",
                            isNegative = true
                        )
                        Divider(modifier = Modifier.padding(vertical = 8.dp))

                        DailyUtilityCard(
                            utilidad = currencyFormatter.format(uiState.utilidadDelDia),
                            margen = String.format("%.1f%% margen", uiState.margenUtilidad)
                        )
                    }
                }
            }

            item {
                Button(
                    onClick = { viewModel.cerrarCajaDelDia() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !uiState.isLoading
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Icon(
                            imageVector = Icons.Default.DoneAll,
                            contentDescription = null,
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Cerrar Caja del Día", fontSize = 18.sp)
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null,
                        tint = Color(0xFF333333)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Historial de Cierres",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF333333)
                    )
                }
            }

            items(uiState.historialCierres) { cierre ->
                HistorialCierreCard(
                    cierre = cierre,
                    currencyFormatter = currencyFormatter,
                    dateFormatter = historialDateFormatter,
                    onVerDetalleClick = { /* TODO: Implementar navegación a detalles */ }
                )
            }
        }
    }
}

@Composable
fun FinancialSummaryRow(
    icon: @Composable () -> Unit,
    iconTint: Color,
    label: String,
    value: String,
    transactions: String,
    isNegative: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = iconTint.copy(alpha = 0.1f),
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon.javaClass.getMethod("get" + icon.javaClass.simpleName.substringBefore("Vector").replace("Filled", "Filled")).invoke(icon) as ImageVector,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = label, fontSize = 16.sp, color = Color(0xFF333333))
                Text(text = transactions, fontSize = 12.sp, color = Color.Gray)
            }
        }
        Text(
            text = value,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = if (isNegative) Color(0xFFD32F2F) else Color(0xFF333333)
        )
    }
}

@Composable
fun FinancialSummaryRow(
    icon: ImageVector,
    iconTint: Color,
    label: String,
    value: String,
    transactions: String,
    isNegative: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = iconTint.copy(alpha = 0.1f),
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = label, fontSize = 16.sp, color = Color(0xFF333333))
                Text(text = transactions, fontSize = 12.sp, color = Color.Gray)
            }
        }
        Text(
            text = value,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = if (isNegative) Color(0xFFD32F2F) else Color(0xFF333333)
        )
    }
}


@Composable
fun DailyUtilityCard(utilidad: String, margen: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1976D2)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Utilidad del Día",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 14.sp
                )
                Text(
                    text = "Ventas - Gastos",
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = utilidad,
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color.White.copy(alpha = 0.2f)
            ) {
                Text(
                    text = margen,
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun HistorialCierreCard(
    cierre: CierreDiarioHistorico,
    currencyFormatter: NumberFormat,
    dateFormatter: SimpleDateFormat,
    onVerDetalleClick: (CierreDiarioHistorico) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = null,
                        tint = Color(0xFF333333),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = dateFormatter.format(Date(cierre.fechaCierre)),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF333333)
                    )
                }
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFE8F5E9)
                ) {
                    Text(
                        text = cierre.estado,
                        color = Color(0xFF4CAF50),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Divider(color = Color(0xFFEEEEEE), thickness = 1.dp)
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = "Ventas", fontSize = 12.sp, color = Color.Gray)
                    Text(
                        text = currencyFormatter.format(cierre.ventas),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF333333)
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Gastos", fontSize = 12.sp, color = Color.Gray)
                    Text(
                        text = currencyFormatter.format(cierre.gastos),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFD32F2F)
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(text = "Utilidad", fontSize = 12.sp, color = Color.Gray)
                    Text(
                        text = currencyFormatter.format(cierre.utilidad),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF4CAF50)
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            TextButton(onClick = { onVerDetalleClick(cierre) }) {
                Text("Ver Detalle", color = Color(0xFF1976D2))
            }
        }
    }
}
