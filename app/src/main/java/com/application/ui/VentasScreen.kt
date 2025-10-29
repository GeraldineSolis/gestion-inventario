package com.application.ui

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.application.model.Producto
import com.application.viewmodel.CartItem
import com.application.viewmodel.ProductViewModel
import com.application.viewmodel.VentasViewModel
import java.text.NumberFormat

@Composable
fun VentasScreen() {
    val application = LocalContext.current.applicationContext as Application
    val productVm: ProductViewModel = viewModel(factory = ViewModelProvider.AndroidViewModelFactory.getInstance(application))
    val ventasVm: VentasViewModel = viewModel(factory = ViewModelProvider.AndroidViewModelFactory.getInstance(application))

    val ui by productVm.uiState.collectAsState()
    val cart by ventasVm.cart.collectAsState()
    val isProcessing by ventasVm.isProcessing.collectAsState()
    val message by ventasVm.message.collectAsState()
    var query by remember { mutableStateOf("") }
    val currency = remember { NumberFormat.getCurrencyInstance() }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(message) {
        message?.let {
            snackbarHostState.showSnackbar(it)
            ventasVm.clearMessage()
        }
    }

    val productosFiltrados = remember(ui.productos, query) {
        if (query.isBlank()) ui.productos
        else ui.productos.filter { it.nombre.contains(query, ignoreCase = true) || it.descripcion.contains(query, true) }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { scaffoldPadding ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
                .padding(scaffoldPadding)
                .background(Color(0xFFF5F7FA)),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Izquierda: Búsqueda + Lista de productos
            Column(modifier = Modifier.weight(1f)) {
                SectionHeader(title = "Buscar Productos", color = Color(0xFF1E88E5), icon = Icons.Filled.Search)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    OutlinedTextField(
                        value = query,
                        onValueChange = { query = it },
                        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                        placeholder = { Text("Nombre del producto o código de barras...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    )
                }
                Spacer(Modifier.height(12.dp))
                Card(
                    modifier = Modifier.fillMaxSize(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Text(
                        "Productos Disponibles",
                        modifier = Modifier.padding(16.dp),
                        fontWeight = FontWeight.SemiBold
                    )
                    Divider(color = Color(0xFFE8E8E8))
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(productosFiltrados) { p ->
                            ProductoRow(
                                producto = p,
                                currency = currency,
                                onAdd = { ventasVm.addOrIncrement(p.id, p.nombre, p.precio, p.stockActual) }
                            )
                            Divider(color = Color(0xFFE8E8E8))
                        }
                    }
                }
            }

            // Derecha: Carrito
            Column(modifier = Modifier.weight(1f)) {
                SectionHeader(title = "Carrito de Venta", color = Color(0xFF2E7D32), icon = Icons.Filled.ShoppingCart)
                Card(
                    modifier = Modifier.fillMaxSize(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    if (cart.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Filled.ShoppingCart, contentDescription = null, tint = Color(0xFFB0BEC5), modifier = Modifier.size(48.dp))
                                Spacer(Modifier.height(8.dp))
                                Text("El carrito está vacío", color = Color(0xFF6B7280))
                                Text("Busca y agrega productos para comenzar", color = Color(0xFF9CA3AF), fontSize = 12.sp)
                            }
                        }
                    } else {
                        Column(modifier = Modifier.fillMaxSize()) {
                            LazyColumn(modifier = Modifier.weight(1f)) {
                                items(cart) { item ->
                                    CartRow(
                                        item = item,
                                        currency = currency,
                                        onInc = { ventasVm.addOrIncrement(item.id, item.nombre, item.precio, item.stockActual) },
                                        onDec = { ventasVm.decrement(item.id) },
                                        onRemove = { ventasVm.remove(item.id) }
                                    )
                                    Divider(color = Color(0xFFE8E8E8))
                                }
                            }
                            val total = cart.sumOf { it.precio * it.cantidad }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("Total", fontWeight = FontWeight.SemiBold)
                                    Text(currency.format(total), fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E88E5))
                                }
                                Button(onClick = { ventasVm.checkout() }, enabled = cart.isNotEmpty() && !isProcessing) {
                                    Text("Pagar")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String, color: Color, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Surface(
        color = color,
        shape = RoundedCornerShape(8.dp),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = Color.White)
            Spacer(Modifier.width(8.dp))
            Text(title, color = Color.White, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun ProductoRow(producto: Producto, currency: NumberFormat, onAdd: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text(producto.nombre, fontWeight = FontWeight.SemiBold)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(currency.format(producto.precio), color = Color(0xFF1E88E5))
                Spacer(Modifier.width(8.dp))
                AssistChip(
                    onClick = {},
                    label = { Text("Stock: ${producto.stockActual}") },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = if (producto.stockActual <= producto.stockMinimo) Color(0xFFFFE4D6) else Color(0xFFE8F5E9)
                    )
                )
            }
        }
        FilledIconButton(onClick = onAdd, modifier = Modifier.size(36.dp)) {
            Icon(Icons.Filled.Add, contentDescription = "Agregar")
        }
    }
}

@Composable
private fun CartRow(
    item: CartItem,
    currency: NumberFormat,
    onInc: () -> Unit,
    onDec: () -> Unit,
    onRemove: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text(item.nombre, fontWeight = FontWeight.Medium)
            Text(currency.format(item.precio))
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            FilledIconButton(onClick = onDec, modifier = Modifier.size(32.dp)) { Icon(Icons.Filled.Remove, contentDescription = null) }
            Text("${item.cantidad}", modifier = Modifier.padding(horizontal = 8.dp))
            FilledIconButton(onClick = onInc, modifier = Modifier.size(32.dp)) { Icon(Icons.Filled.Add, contentDescription = null) }
            Spacer(Modifier.width(8.dp))
            IconButton(onClick = onRemove) { Icon(Icons.Filled.Delete, contentDescription = null, tint = Color(0xFFD32F2F)) }
        }
    }
}