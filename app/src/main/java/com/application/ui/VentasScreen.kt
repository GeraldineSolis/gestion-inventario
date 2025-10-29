package com.application.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.HorizontalRule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.application.model.Producto
import com.application.viewmodel.ProductViewModel
import com.application.viewmodel.CarritoItem
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VentasScreen(
    viewModel: ProductViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var carrito by remember { mutableStateOf<List<CarritoItem>>(emptyList()) }

    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    var showBottomSheet by remember { mutableStateOf(false) }

    val totalCarrito = carrito.sumOf { (it.producto.precio ?: 0.0) * it.cantidad }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1976D2))
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Buscar Productos",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = {
                    Text(
                        "Nombre del producto o código de barras",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                singleLine = true
            )
        }

        Text(
            text = "Productos Disponibles",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF333333),
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val filteredProducts = if (searchQuery.isBlank()) {
                    uiState.productos
                } else {
                    uiState.productos.filter {
                        it.nombre.contains(searchQuery, ignoreCase = true)
                    }
                }

                items(filteredProducts) { producto ->
                    ProductoVentaCard(
                        producto = producto,
                        onAddToCart = { prod ->
                            val existing = carrito.find { it.producto.id == prod.id }
                            carrito = if (existing != null) {
                                carrito.map {
                                    if (it.producto.id == prod.id)
                                        it.copy(cantidad = it.cantidad + 1)
                                    else it
                                }
                            } else {
                                carrito + CarritoItem(prod, 1)
                            }
                        }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }

        if (carrito.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF4CAF50)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                onClick = {
                    showBottomSheet = true
                }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Carrito de Venta",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${carrito.sumOf { it.cantidad }} items",
                                color = Color.White.copy(alpha = 0.9f),
                                fontSize = 12.sp
                            )
                        }
                    }

                    Text(
                        text = "$${String.format("%.2f", totalCarrito)}",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
            },
            sheetState = sheetState
        ) {
            CarritoContent(
                carritoItems = carrito,
                total = totalCarrito,
                onQuantityChange = { item, newAmount ->
                    carrito = carrito.map {
                        if (it.producto.id == item.producto.id)
                            it.copy(cantidad = newAmount)
                        else it
                    }.filter { it.cantidad > 0 }
                },
                onRemoveItem = { item ->
                    carrito = carrito.filter { it.producto.id != item.producto.id }
                },
                onProcessSale = {
                    viewModel.processSale(carrito)

                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            showBottomSheet = false
                            carrito = emptyList()
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun ProductoVentaCard(
    producto: Producto,
    onAddToCart: (Producto) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = producto.nombre,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF333333)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$${String.format("%.2f", producto.precio ?: 0.0)}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1976D2)
                )
                Spacer(modifier = Modifier.height(4.dp))

                val stockColor = when {
                    producto.stockActual == 0 -> Color(0xFFD32F2F)
                    producto.stockActual <= 5 -> Color(0xFFFF9800)
                    producto.stockActual <= 20 -> Color(0xFF4CAF50)
                    else -> Color(0xFF2196F3)
                }

                Surface(
                    color = stockColor.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Stock: ${producto.stockActual}",
                        color = stockColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            IconButton(
                onClick = { onAddToCart(producto) },
                enabled = producto.stockActual > 0,
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = if (producto.stockActual > 0) Color(0xFF1976D2) else Color.Gray,
                        shape = RoundedCornerShape(24.dp)
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar al carrito",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun CarritoContent(
    carritoItems: List<CarritoItem>,
    total: Double,
    onQuantityChange: (CarritoItem, Int) -> Unit,
    onRemoveItem: (CarritoItem) -> Unit,
    onProcessSale: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = null,
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Carrito de Venta",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333)
                )
            }
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFF4CAF50)
            ) {
                Text(
                    text = carritoItems.sumOf { it.cantidad }.toString(),
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxHeight(0.5f),
            verticalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            items(carritoItems) { item ->
                CarritoItemRow(
                    item = item,
                    onQuantityChange = onQuantityChange,
                    onRemoveItem = onRemoveItem
                )
                Divider(color = Color(0xFFEEEEEE), thickness = 1.dp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Subtotal:",
                fontSize = 18.sp,
                color = Color.Gray
            )
            Text(
                text = "$${String.format("%.2f", total)}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onProcessSale,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.ShoppingCart,
                contentDescription = null,
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Procesar Venta", fontSize = 18.sp)
        }
    }
}

@Composable
fun CarritoItemRow(
    item: CarritoItem,
    onQuantityChange: (CarritoItem, Int) -> Unit,
    onRemoveItem: (CarritoItem) -> Unit
) {
    val totalItem = (item.producto.precio ?: 0.0) * item.cantidad

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.producto.nombre,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "$${String.format("%.2f", item.producto.precio ?: 0.0)} c/u",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }

        IconButton(onClick = { onRemoveItem(item) }) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Eliminar",
                tint = Color(0xFFD32F2F)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.width(110.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = { if (item.cantidad > 1) onQuantityChange(item, item.cantidad - 1) else onRemoveItem(item) },
                modifier = Modifier.size(30.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.HorizontalRule,
                    contentDescription = "Restar",
                    tint = Color(0xFF1976D2)
                )            }
            Text(
                text = item.cantidad.toString(),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            IconButton(
                onClick = { onQuantityChange(item, item.cantidad + 1) },
                modifier = Modifier.size(30.dp),
                enabled = item.cantidad < (item.producto.stockActual ?: 99999)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Sumar", tint = Color(0xFF1976D2))
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = "$${String.format("%.2f", totalItem)}",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1976D2),
            modifier = Modifier.width(60.dp)
        )
    }
}