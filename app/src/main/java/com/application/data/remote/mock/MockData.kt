package com.application.data.remote.mock

import com.application.data.remote.dto.CompraDto
import com.application.data.remote.dto.ProductoDto
import java.util.Calendar

object MockData {

    val productosBase = listOf(
        ProductoDto(
            id = 1,
            nombre = "Coca Cola 600ml",
            descripcion = "Gaseosa de cola, botella personal de 600ml",
            precio = 3.50,
            stockActual = 45,
            stockMinimo = 20,
            categoria = "Bebidas",
            fechaCreacion = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000)
        ),
        ProductoDto(
            id = 2,
            nombre = "Galletas Oreo",
            descripcion = "Galletas de chocolate con crema, paquete mediano",
            precio = 5.50,
            stockActual = 5,
            stockMinimo = 15,
            categoria = "Snacks",
            fechaCreacion = System.currentTimeMillis() - (10 * 24 * 60 * 60 * 1000)
        ),
        ProductoDto(
            id = 3,
            nombre = "Detergente Ace 1kg",
            descripcion = "Detergente en polvo para ropa, presentación 1kg",
            precio = 15.90,
            stockActual = 2,
            stockMinimo = 10,
            categoria = "Limpieza",
            fechaCreacion = System.currentTimeMillis() - (15 * 24 * 60 * 60 * 1000)
        ),
        ProductoDto(
            id = 4,
            nombre = "Pan de Molde Bimbo",
            descripcion = "Pan de molde blanco grande",
            precio = 7.90,
            stockActual = 20,
            stockMinimo = 10,
            categoria = "Panadería",
            fechaCreacion = System.currentTimeMillis() - (3 * 24 * 60 * 60 * 1000)
        ),
        ProductoDto(
            id = 5,
            nombre = "Leche Gloria/Laive 1L",
            descripcion = "Leche entera UHT, caja de 1 litro",
            precio = 4.50,
            stockActual = 30,
            stockMinimo = 25,
            categoria = "Lácteos",
            fechaCreacion = System.currentTimeMillis() - (5 * 24 * 60 * 60 * 1000)
        ),
        ProductoDto(
            id = 6,
            nombre = "Papas Lays Clásicas 45g",
            descripcion = "Papas fritas sabor original, bolsa personal",
            precio = 2.00,
            stockActual = 60,
            stockMinimo = 30,
            categoria = "Snacks",
            fechaCreacion = System.currentTimeMillis() - (4 * 24 * 60 * 60 * 1000)
        ),
        ProductoDto(
            id = 7,
            nombre = "Agua San Mateo/Cielo 1.5L",
            descripcion = "Agua de mesa purificada, botella de 1.5 litros",
            precio = 2.50,
            stockActual = 8,
            stockMinimo = 40,
            categoria = "Bebidas",
            fechaCreacion = System.currentTimeMillis() - (6 * 24 * 60 * 60 * 1000)
        ),
        ProductoDto(
            id = 8,
            nombre = "Arroz Costeño Superior 1kg",
            descripcion = "Arroz extra, bolsa de 1kg",
            precio = 5.00,
            stockActual = 35,
            stockMinimo = 15,
            categoria = "Abarrotes",
            fechaCreacion = System.currentTimeMillis() - (12 * 24 * 60 * 60 * 1000)
        ),
        ProductoDto(
            id = 9,
            nombre = "Papel Higiénico Paracas 4 rollos",
            descripcion = "Papel higiénico doble hoja, paquete de 4 rollos",
            precio = 7.50,
            stockActual = 3,
            stockMinimo = 12,
            categoria = "Limpieza",
            fechaCreacion = System.currentTimeMillis() - (8 * 24 * 60 * 60 * 1000)
        ),
        ProductoDto(
            id = 10,
            nombre = "Huevo de Gallina 12 piezas",
            descripcion = "Huevo blanco/color, paquete de 12 unidades (docena)",
            precio = 6.00,
            stockActual = 25,
            stockMinimo = 20,
            categoria = "Abarrotes",
            fechaCreacion = System.currentTimeMillis() - (2 * 24 * 60 * 60 * 1000)
        ),
        ProductoDto(
            id = 11,
            nombre = "Jabón Bolívar/Marsella 200g",
            descripcion = "Jabón de barra para lavar ropa o multiuso",
            precio = 3.00,
            stockActual = 18,
            stockMinimo = 10,
            categoria = "Limpieza",
            fechaCreacion = System.currentTimeMillis() - (9 * 24 * 60 * 60 * 1000)
        ),
        ProductoDto(
            id = 12,
            nombre = "Aceite Primor 1L",
            descripcion = "Aceite vegetal comestible, botella de 1 litro",
            precio = 8.90,
            stockActual = 15,
            stockMinimo = 10,
            categoria = "Abarrotes",
            fechaCreacion = System.currentTimeMillis() - (11 * 24 * 60 * 60 * 1000)
        )
    )

    val comprasBase = listOf(
        CompraDto(
            id = 1,
            productoId = 1,
            nombreProducto = "Coca Cola 600ml",
            cantidad = 50,
            precioUnitario = 3.00,
            fechaCompra = getDateDaysAgo(8),
        ),
        CompraDto(
            id = 2,
            productoId = 2,
            nombreProducto = "Galletas Oreo",
            cantidad = 30,
            precioUnitario = 4.80,
            fechaCompra = getDateDaysAgo(12),
        ),
        CompraDto(
            id = 3,
            productoId = 3,
            nombreProducto = "Detergente Ace 1kg",
            cantidad = 15,
            precioUnitario = 14.50,
            fechaCompra = getDateDaysAgo(16),
        ),
        CompraDto(
            id = 4,
            productoId = 4,
            nombreProducto = "Pan de Molde Bimbo",
            cantidad = 40,
            precioUnitario = 7.00,
            fechaCompra = getDateDaysAgo(4),
        ),
        CompraDto(
            id = 5,
            productoId = 5,
            nombreProducto = "Leche Gloria/Laive 1L",
            cantidad = 60,
            precioUnitario = 4.00,
            fechaCompra = getDateDaysAgo(6),
        ),
        CompraDto(
            id = 6,
            productoId = 6,
            nombreProducto = "Papas Lays Clásicas 45g",
            cantidad = 100,
            precioUnitario = 1.70,
            fechaCompra = getDateDaysAgo(5),
        ),
        CompraDto(
            id = 7,
            productoId = 7,
            nombreProducto = "Agua San Mateo/Cielo 1.5L",
            cantidad = 80,
            precioUnitario = 2.00,
            fechaCompra = getDateDaysAgo(7),
        ),
        CompraDto(
            id = 8,
            productoId = 8,
            nombreProducto = "Arroz Costeño Superior 1kg",
            cantidad = 50,
            precioUnitario = 4.50,
            fechaCompra = getDateDaysAgo(13),
        ),
        CompraDto(
            id = 9,
            productoId = 10,
            nombreProducto = "Huevo de Gallina 12 piezas",
            cantidad = 35,
            precioUnitario = 5.50,
            fechaCompra = getDateDaysAgo(3),
        ),
        CompraDto(
            id = 10,
            productoId = 1,
            nombreProducto = "Coca Cola 600ml",
            cantidad = 30,
            precioUnitario = 3.10,
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