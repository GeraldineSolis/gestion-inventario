package com.application.ui

import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.application.ui.theme.DesignTokens

@Composable
fun ProductEntryScreen() {
    val context = LocalContext.current
    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var costo by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("0") }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(DesignTokens.AppBackground)
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = DesignTokens.CardElevation),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF1E88E5))
                            .padding(horizontal = 16.dp, vertical = 14.dp)
                    ) {
                        Column {
                            Text(
                                text = "Registro de Producto",
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "Formulario optimizado para entrada rápida de datos",
                                color = Color.White.copy(alpha = 0.85f),
                                fontSize = 12.sp
                            )
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(140.dp)
                        ) {
                            Canvas(modifier = Modifier.matchParentSize()) {
                                val stroke = Stroke(width = 2.dp.toPx(), pathEffect = PathEffect.dashPathEffect(floatArrayOf(14f, 10f), 0f))
                                drawRoundRect(
                                    color = Color(0xFF1E88E5).copy(alpha = 0.6f),
                                    style = stroke,
                                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(16.dp.toPx(), 16.dp.toPx())
                                )
                                val innerPadding = 16.dp.toPx()
                                drawRoundRect(
                                    color = Color(0xFF1E88E5).copy(alpha = 0.45f),
                                    topLeft = androidx.compose.ui.geometry.Offset(innerPadding, innerPadding),
                                    size = androidx.compose.ui.geometry.Size(size.width - innerPadding * 2, size.height - innerPadding * 2),
                                    style = stroke,
                                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(12.dp.toPx(), 12.dp.toPx())
                                )
                            }
                            Column(
                                Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(Icons.Filled.Add, contentDescription = null, tint = Color(0xFF1E88E5))
                                Spacer(Modifier.height(6.dp))
                                Text("Escanear Código de Barras", color = DesignTokens.TextSecondary)
                            }
                        }

                        OutlinedTextField(
                            value = nombre,
                            onValueChange = { nombre = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Nombre del Producto *") },
                            placeholder = { Text("Ej: Coca Cola 600ml") },
                            singleLine = true
                        )

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            OutlinedTextField(
                                value = precio,
                                onValueChange = { precio = it },
                                modifier = Modifier.weight(1f),
                                label = { Text("Precio de Venta *") },
                                leadingIcon = { Text("$") }
                            )
                            OutlinedTextField(
                                value = costo,
                                onValueChange = { costo = it },
                                modifier = Modifier.weight(1f),
                                label = { Text("Costo *") },
                                leadingIcon = { Text("$") }
                            )
                        }

                        OutlinedTextField(
                            value = stock,
                            onValueChange = { stock = it.filter { c -> c.isDigit() } },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Stock Inicial *") },
                            singleLine = true
                        )

                        Divider(color = DesignTokens.Divider)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    val activity = context as? ComponentActivity
                                    activity?.onBackPressedDispatcher?.onBackPressed()
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Cancelar")
                            }

                            Button(
                                onClick = { },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E88E5))
                            ) {
                                Text("Registrar Producto")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductEntryScreenPreview() {
    ProductEntryScreen()
}