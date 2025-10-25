package com.application.data.remote.mock

import com.application.data.remote.dto.CompraDto
import com.application.data.remote.dto.ProductoDto
import java.util.Calendar

object MockData {

    // === PRODUCTOS MOCK ===
    val productosBase = listOf(
        ProductoDto(
            id = 1,
            nombre = "Coca Cola 600ml",
            descripcion = "Refresco de cola, botella de 600ml",
            precio = 25.0,
            stockActual = 45,
            stockMinimo = 20,
            categoria = "Bebidas",
            fechaCreacion = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000) // Hace 7 días
        ),
        ProductoDto(
            id = 2,
            nombre = "Galletas Oreo",
            descripcion = "Galletas de chocolate con crema, paquete familiar",
            precio = 32.5,
            stockActual = 5,
            stockMinimo = 15,
            categoria = "Snacks",
            fechaCreacion = System.currentTimeMillis() - (10 * 24 * 60 * 60 * 1000)
        ),
        ProductoDto(
            id = 3,
            nombre = "Detergente Ace 1kg",
            descripcion = "Detergente en polvo para ropa, presentación 1kg",
            precio = 78.0,
            stockActual = 2,
            stockMinimo = 10,
            categoria = "Limpieza",
            fechaCreacion = System.currentTimeMillis() - (15 * 24 * 60 * 60 * 1000)
        ),
        ProductoDto(
            id = 4,
            nombre = "Pan Bimbo Blanco",
            descripcion = "Pan de caja blanco grande",
            precio = 42.0,
            stockActual = 20,
            stockMinimo = 10,
            categoria = "Panadería",
            fechaCreacion = System.currentTimeMillis() - (3 * 24 * 60 * 60 * 1000)
        ),
        ProductoDto(
            id = 5,
            nombre = "Leche Lala 1L",
            descripcion = "Leche entera ultra pasteurizada, 1 litro",
            precio = 28.5,
            stockActual = 30,
            stockMinimo = 25,
            categoria = "Lácteos",
            fechaCreacion = System.currentTimeMillis() - (5 * 24 * 60 * 60 * 1000)
        ),
        ProductoDto(
            id = 6,
            nombre = "Sabritas Original 45g",
            descripcion = "Papas fritas sabor original",
            precio = 18.0,
            stockActual = 60,
            stockMinimo = 30,
            categoria = "Snacks",
            fechaCreacion = System.currentTimeMillis() - (4 * 24 * 60 * 60 * 1000)
        ),
        ProductoDto(
            id = 7,
            nombre = "Agua Ciel 1.5L",
            descripcion = "Agua purificada, botella de 1.5 litros",
            precio = 12.0,
            stockActual = 8,
            stockMinimo = 40,
            categoria = "Bebidas",
            fechaCreacion = System.currentTimeMillis() - (6 * 24 * 60 * 60 * 1000)
        ),
        ProductoDto(
            id = 8,
            nombre = "Arroz Verde Valle 1kg",
            descripcion = "Arroz blanco extra fino, bolsa de 1kg",
            precio = 24.0,
            stockActual = 35,
            stockMinimo = 15,
            categoria = "Abarrotes",
            fechaCreacion = System.currentTimeMillis() - (12 * 24 * 60 * 60 * 1000)
        ),
        ProductoDto(
            id = 9,
            nombre = "Papel Higiénico Suave 4 rollos",
            descripcion = "Papel higiénico doble hoja, paquete de 4 rollos",
            precio = 45.0,
            stockActual = 3,
            stockMinimo = 12,
            categoria = "Limpieza",
            fechaCreacion = System.currentTimeMillis() - (8 * 24 * 60 * 60 * 1000)
        ),
        ProductoDto(
            id = 10,
            nombre = "Huevo Blanco 12 piezas",
            descripcion = "Huevo blanco de rancho, paquete de 12 piezas",
            precio = 52.0,
            stockActual = 25,
            stockMinimo = 20,
            categoria = "Abarrotes",
            fechaCreacion = System.currentTimeMillis() - (2 * 24 * 60 * 60 * 1000)
        ),
        ProductoDto(
            id = 11,
            nombre = "Jabón Zote 200g",
            descripcion = "Jabón de barra para lavar ropa",
            precio = 15.0,
            stockActual = 18,
            stockMinimo = 10,
            categoria = "Limpieza",
            fechaCreacion = System.currentTimeMillis() - (9 * 24 * 60 * 60 * 1000)
        ),
        ProductoDto(
            id = 12,
            nombre = "Aceite Capullo 1L",
            descripcion = "Aceite vegetal comestible, botella de 1 litro",
            precio = 38.0,
            stockActual = 15,
            stockMinimo = 10,
            categoria = "Abarrotes",
            fechaCreacion = System.currentTimeMillis() - (11 * 24 * 60 * 60 * 1000)
        )
    )

    // === COMPRAS MOCK ===
    val comprasBase = listOf(
        CompraDto(
            id = 1,
            productoId = 1,
            nombreProducto = "Coca Cola 600ml",
            cantidad = 50,
            precioUnitario = 22.0,
            fechaCompra = getDateDaysAgo(8),
        ),
        CompraDto(
            id = 2,
            productoId = 2,
            nombreProducto = "Galletas Oreo",
            cantidad = 30,
            precioUnitario = 28.0,
            fechaCompra = getDateDaysAgo(12),
        ),
        CompraDto(
            id = 3,
            productoId = 3,
            nombreProducto = "Detergente Ace 1kg",
            cantidad = 15,
            precioUnitario = 70.0,
            fechaCompra = getDateDaysAgo(16),
        ),
        CompraDto(
            id = 4,
            productoId = 4,
            nombreProducto = "Pan Bimbo Blanco",
            cantidad = 40,
            precioUnitario = 38.0,
            fechaCompra = getDateDaysAgo(4),
        ),
        CompraDto(
            id = 5,
            productoId = 5,
            nombreProducto = "Leche Lala 1L",
            cantidad = 60,
            precioUnitario = 25.0,
            fechaCompra = getDateDaysAgo(6),
        ),
        CompraDto(
            id = 6,
            productoId = 6,
            nombreProducto = "Sabritas Original 45g",
            cantidad = 100,
            precioUnitario = 15.0,
            fechaCompra = getDateDaysAgo(5),
        ),
        CompraDto(
            id = 7,
            productoId = 7,
            nombreProducto = "Agua Ciel 1.5L",
            cantidad = 80,
            precioUnitario = 9.0,
            fechaCompra = getDateDaysAgo(7),
        ),
        CompraDto(
            id = 8,
            productoId = 8,
            nombreProducto = "Arroz Verde Valle 1kg",
            cantidad = 50,
            precioUnitario = 20.0,
            fechaCompra = getDateDaysAgo(13),
        ),
        CompraDto(
            id = 9,
            productoId = 10,
            nombreProducto = "Huevo Blanco 12 piezas",
            cantidad = 35,
            precioUnitario = 48.0,
            fechaCompra = getDateDaysAgo(3),
        ),
        CompraDto(
            id = 10,
            productoId = 1,
            nombreProducto = "Coca Cola 600ml",
            cantidad = 30,
            precioUnitario = 22.5,
            fechaCompra = getDateDaysAgo(1),
        )
    )

    // Función auxiliar para generar fechas pasadas
    private fun getDateDaysAgo(days: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -days)
        return calendar.timeInMillis
    }

    var nextProductoId = 13
    var nextCompraId = 11

    val productosMock = productosBase.toMutableList()
    val comprasMock = comprasBase.toMutableList()
}