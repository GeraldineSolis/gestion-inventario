package com.application.data.remote.mock

import com.application.data.remote.api.InventarioApiService
import com.application.data.remote.dto.ApiResponse
import com.application.data.remote.dto.CompraDto
import com.application.data.remote.dto.ProductoDto
import kotlinx.coroutines.delay
import retrofit2.Response

class MockApiService : InventarioApiService {

    // Simula latencia de red (300-800ms)
    private suspend fun simulateNetworkDelay() {
        delay((300..800).random().toLong())
    }

    // === PRODUCTOS ===

    override suspend fun getAllProductos(): Response<ApiResponse<List<ProductoDto>>> {
        simulateNetworkDelay()
        return Response.success(
            ApiResponse(
                success = true,
                data = MockData.productosMock.toList(),
                message = "Productos obtenidos exitosamente"
            )
        )
    }

    override suspend fun getProductoById(id: Int): Response<ApiResponse<ProductoDto>> {
        simulateNetworkDelay()
        val producto = MockData.productosMock.find { it.id == id }
        return if (producto != null) {
            Response.success(
                ApiResponse(
                    success = true,
                    data = producto,
                    message = "Producto encontrado"
                )
            )
        } else {
            Response.success(
                ApiResponse(
                    success = false,
                    data = null,
                    error = "Producto no encontrado"
                )
            )
        }
    }

    override suspend fun createProducto(producto: ProductoDto): Response<ApiResponse<ProductoDto>> {
        simulateNetworkDelay()
        val nuevoProducto = producto.copy(
            id = MockData.nextProductoId++,
            fechaCreacion = System.currentTimeMillis()
        )
        MockData.productosMock.add(nuevoProducto)
        return Response.success(
            ApiResponse(
                success = true,
                data = nuevoProducto,
                message = "Producto creado exitosamente"
            )
        )
    }

    override suspend fun updateProducto(id: Int, producto: ProductoDto): Response<ApiResponse<ProductoDto>> {
        simulateNetworkDelay()
        val index = MockData.productosMock.indexOfFirst { it.id == id }
        return if (index != -1) {
            val productoActualizado = producto.copy(id = id)
            MockData.productosMock[index] = productoActualizado
            Response.success(
                ApiResponse(
                    success = true,
                    data = productoActualizado,
                    message = "Producto actualizado exitosamente"
                )
            )
        } else {
            Response.success(
                ApiResponse(
                    success = false,
                    data = null,
                    error = "Producto no encontrado"
                )
            )
        }
    }

    override suspend fun deleteProducto(id: Int): Response<ApiResponse<Unit>> {
        simulateNetworkDelay()
        val removed = MockData.productosMock.removeIf { it.id == id }
        return if (removed) {
            Response.success(
                ApiResponse(
                    success = true,
                    data = Unit,
                    message = "Producto eliminado exitosamente"
                )
            )
        } else {
            Response.success(
                ApiResponse(
                    success = false,
                    data = null,
                    error = "Producto no encontrado"
                )
            )
        }
    }

    override suspend fun getProductosByCategoria(categoria: String): Response<ApiResponse<List<ProductoDto>>> {
        simulateNetworkDelay()
        val productosFiltrados = MockData.productosMock.filter {
            it.categoria.equals(categoria, ignoreCase = true)
        }
        return Response.success(
            ApiResponse(
                success = true,
                data = productosFiltrados,
                message = "Productos filtrados por categoría"
            )
        )
    }

    override suspend fun getProductosBajoStock(): Response<ApiResponse<List<ProductoDto>>> {
        simulateNetworkDelay()
        val productosBajoStock = MockData.productosMock.filter {
            it.stockActual <= it.stockMinimo
        }
        return Response.success(
            ApiResponse(
                success = true,
                data = productosBajoStock,
                message = "Productos con stock bajo"
            )
        )
    }

    // === COMPRAS ===

    override suspend fun getAllCompras(): Response<ApiResponse<List<CompraDto>>> {
        simulateNetworkDelay()
        return Response.success(
            ApiResponse(
                success = true,
                data = MockData.comprasMock.sortedByDescending { it.fechaCompra },
                message = "Compras obtenidas exitosamente"
            )
        )
    }

    override suspend fun getCompraById(id: Int): Response<ApiResponse<CompraDto>> {
        simulateNetworkDelay()
        val compra = MockData.comprasMock.find { it.id == id }
        return if (compra != null) {
            Response.success(
                ApiResponse(
                    success = true,
                    data = compra,
                    message = "Compra encontrada"
                )
            )
        } else {
            Response.success(
                ApiResponse(
                    success = false,
                    data = null,
                    error = "Compra no encontrada"
                )
            )
        }
    }

    override suspend fun createCompra(compra: CompraDto): Response<ApiResponse<CompraDto>> {
        simulateNetworkDelay()
        val nuevaCompra = compra.copy(
            id = MockData.nextCompraId++,
            fechaCompra = System.currentTimeMillis()
        )
        MockData.comprasMock.add(nuevaCompra)

        // Actualizar stock del producto automáticamente
        val productoIndex = MockData.productosMock.indexOfFirst { it.id == compra.productoId }
        if (productoIndex != -1) {
            val producto = MockData.productosMock[productoIndex]
            MockData.productosMock[productoIndex] = producto.copy(
                stockActual = producto.stockActual + compra.cantidad
            )
        }

        return Response.success(
            ApiResponse(
                success = true,
                data = nuevaCompra,
                message = "Compra registrada exitosamente"
            )
        )
    }

    override suspend fun updateCompra(id: Int, compra: CompraDto): Response<ApiResponse<CompraDto>> {
        simulateNetworkDelay()
        val index = MockData.comprasMock.indexOfFirst { it.id == id }
        return if (index != -1) {
            val compraActualizada = compra.copy(id = id)
            MockData.comprasMock[index] = compraActualizada
            Response.success(
                ApiResponse(
                    success = true,
                    data = compraActualizada,
                    message = "Compra actualizada exitosamente"
                )
            )
        } else {
            Response.success(
                ApiResponse(
                    success = false,
                    data = null,
                    error = "Compra no encontrada"
                )
            )
        }
    }

    override suspend fun deleteCompra(id: Int): Response<ApiResponse<Unit>> {
        simulateNetworkDelay()
        val removed = MockData.comprasMock.removeIf { it.id == id }
        return if (removed) {
            Response.success(
                ApiResponse(
                    success = true,
                    data = Unit,
                    message = "Compra eliminada exitosamente"
                )
            )
        } else {
            Response.success(
                ApiResponse(
                    success = false,
                    data = null,
                    error = "Compra no encontrada"
                )
            )
        }
    }

    override suspend fun getComprasByProducto(productoId: Int): Response<ApiResponse<List<CompraDto>>> {
        simulateNetworkDelay()
        val comprasFiltradas = MockData.comprasMock.filter {
            it.productoId == productoId
        }.sortedByDescending { it.fechaCompra }
        return Response.success(
            ApiResponse(
                success = true,
                data = comprasFiltradas,
                message = "Compras del producto obtenidas"
            )
        )
    }
}