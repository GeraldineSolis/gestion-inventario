package com.application.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.application.model.Compra
import com.application.viewmodel.PurchaseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GastosScreen(
    viewModel: PurchaseViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Estados del formulario
    var tipoMovimiento by remember { mutableStateOf("Compra de Stock") }
    var descripcion by remember { mutableStateOf("") }
    var montoTotal by remember { mutableStateOf("") }
    var cantidad by remember { mutableStateOf("") }
    var showProductSelector by remember { mutableStateOf(false) }
    var selectedProducto by remember { mutableStateOf<com.application.model.Producto?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFD32F2F))
                .padding(20.dp)
        ) {
            Text(
                text = "Compras/Insumos",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Registro de egresos de dinero",
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 14.sp
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "Tipo de Movimiento *",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF333333),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = tipoMovimiento == "Compra de Stock",
                            onClick = { tipoMovimiento = "Compra de Stock" }
                        ),
                    colors = CardDefaults.cardColors(
                        containerColor = if (tipoMovimiento == "Compra de Stock")
                            Color(0xFFE3F2FD) else Color.White
                    ),
                    shape = RoundedCornerShape(8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                    border = if (tipoMovimiento == "Compra de Stock")
                        androidx.compose.foundation.BorderStroke(2.dp, Color(0xFF1976D2))
                    else
                        androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE0E0E0))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = tipoMovimiento == "Compra de Stock",
                            onClick = { tipoMovimiento = "Compra de Stock" },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = Color(0xFF1976D2)
                            )
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Compra de Stock",
                            fontSize = 16.sp,
                            color = Color(0xFF333333)
                        )
                    }
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = tipoMovimiento == "Gasto de Insumo",
                            onClick = { tipoMovimiento = "Gasto de Insumo" }
                        ),
                    colors = CardDefaults.cardColors(
                        containerColor = if (tipoMovimiento == "Gasto de Insumo")
                            Color(0xFFE3F2FD) else Color.White
                    ),
                    shape = RoundedCornerShape(8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                    border = if (tipoMovimiento == "Gasto de Insumo")
                        androidx.compose.foundation.BorderStroke(2.dp, Color(0xFF1976D2))
                    else
                        androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE0E0E0))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = tipoMovimiento == "Gasto de Insumo",
                            onClick = { tipoMovimiento = "Gasto de Insumo" },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = Color(0xFF1976D2)
                            )
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Gasto de Insumo",
                            fontSize = 16.sp,
                            color = Color(0xFF333333)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Descripción *",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF333333),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                placeholder = {
                    Text(
                        "Ej: Compra de productos para reventa",
                        color = Color.Gray
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF1976D2),
                    unfocusedBorderColor = Color(0xFFE0E0E0)
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Monto Total *",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF333333),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    OutlinedTextField(
                        value = montoTotal,
                        onValueChange = { montoTotal = it },
                        placeholder = { Text("$0.00", color = Color.Gray) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF1976D2),
                            unfocusedBorderColor = Color(0xFFE0E0E0)
                        )
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Cantidad *",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF333333),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    OutlinedTextField(
                        value = cantidad,
                        onValueChange = { cantidad = it },
                        placeholder = { Text("0", color = Color.Gray) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF1976D2),
                            unfocusedBorderColor = Color(0xFFE0E0E0)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        descripcion = ""
                        montoTotal = ""
                        cantidad = ""
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFF666666)
                    )
                ) {
                    Text("Cancelar", fontSize = 16.sp)
                }

                Button(
                    onClick = {
                        if (tipoMovimiento == "Compra de Stock") {
                            val cantidadInt = cantidad.toIntOrNull() ?: 0
                            val precioUnit = montoTotal.toDoubleOrNull()?.div(cantidadInt) ?: 0.0

                            showProductSelector = true
                        }
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFD32F2F)
                    ),
                    enabled = descripcion.isNotBlank() &&
                            montoTotal.isNotBlank() &&
                            cantidad.isNotBlank()
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Registrar Movimiento", fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            uiState.errorMessage?.let { error ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFEBEE)
                    )
                ) {
                    Text(
                        text = error,
                        color = Color(0xFFC62828),
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            uiState.successMessage?.let { success ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFE8F5E9)
                    )
                ) {
                    Text(
                        text = success,
                        color = Color(0xFF2E7D32),
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }

        if (showProductSelector) {
            AlertDialog(
                onDismissRequest = { showProductSelector = false },
                title = { Text("Seleccionar Producto") },
                text = {
                    LazyColumn {
                        items(uiState.productos) { producto ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                onClick = {
                                    selectedProducto = producto

                                    val compra = Compra(
                                        productoId = producto.id,
                                        nombreProducto = producto.nombre,
                                        cantidad = cantidad.toIntOrNull() ?: 0,
                                        precioUnitario = montoTotal.toDoubleOrNull()?.div(cantidad.toIntOrNull() ?: 1) ?: 0.0,
                                    )

                                    viewModel.saveCompra(compra)

                                    // Limpiar formulario
                                    descripcion = ""
                                    montoTotal = ""
                                    cantidad = ""
                                    showProductSelector = false
                                }
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = producto.nombre,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Stock: ${producto.stockActual}",
                                        fontSize = 12.sp,
                                        color = Color.Gray
                                    )
                                }
                            }
                        }
                    }
                },
                confirmButton = {},
                dismissButton = {
                    TextButton(onClick = { showProductSelector = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}