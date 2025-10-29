package com.application.data.repository

import com.application.data.dao.CompraDao
import com.application.data.dao.ProductoDao
import com.application.data.dao.VentaDao
import com.application.data.remote.RetrofitClient
import com.application.data.remote.dto.toDto
import com.application.data.remote.dto.toEntity
import com.application.model.Compra
import com.application.model.Producto
import com.application.model.Venta
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class InventoryRepository(
    private val productoDao: ProductoDao,
    private val ventaDao: VentaDao,
    private val compraDao: CompraDao
) {

    private val apiService = RetrofitClient.apiService

    // OPERACIONES DE PRODUCTOS

    fun getAllProductos(): Flow<Resource<List<Producto>>> = flow {
        emit(Resource.Loading())

        // 1) Intentar refrescar desde API/mock primero
        try {
            val response = apiService.getAllProductos()
            if (response.isSuccessful && response.body()?.success == true) {
                response.body()?.data?.let { productosDto ->
                    val productos = productosDto.map { it.toEntity() }
                    productos.forEach { productoDao.insert(it) }
                }
            }
        } catch (e: Exception) {
            // No interrumpir el flujo: mostramos error pero continuamos emitiendo datos locales
            emit(Resource.Error("Error de red: ${e.message}"))
        }

        // 2) Emitir continuamente los datos locales actualizados
        productoDao.getAll().collect { localProductos ->
            emit(Resource.Success(localProductos))
        }
    }.flowOn(Dispatchers.IO)

    fun getProductoById(id: Int): Flow<Producto?> = productoDao.getById(id)

    fun getProductosByCategoria(categoria: String): Flow<List<Producto>> =
        productoDao.getByCategoria(categoria)

    fun getProductosBajoStock(): Flow<List<Producto>> =
        productoDao.getProductosBajoStock()

    fun getProductosCount(): Flow<Int> = productoDao.getCount()

    suspend fun insertProducto(producto: Producto): Resource<Long> = withContext(Dispatchers.IO) {
        try {
            val localId = productoDao.insert(producto)

            try {
                val response = apiService.createProducto(producto.toDto())
                if (response.isSuccessful && response.body()?.success == true) {
                    response.body()?.data?.let { productoDto ->
                        productoDao.update(productoDto.toEntity())
                    }
                    Resource.Success(localId)
                } else {
                    Resource.Error(
                        message = "Producto guardado localmente, pero error en servidor: ${response.body()?.error}",
                        data = localId
                    )
                }
            } catch (e: IOException) {
                Resource.Error(
                    message = "Producto guardado localmente. Error de conexión: No se pudo conectar al servidor",
                    data = localId
                )
            } catch (e: HttpException) {
                Resource.Error(
                    message = "Producto guardado localmente. Error HTTP ${e.code()}: ${e.message()}",
                    data = localId
                )
            } catch (e: Exception) {
                Resource.Error(
                    message = "Producto guardado localmente. Error inesperado: ${e.message}",
                    data = localId
                )
            }
        } catch (e: Exception) {
            Resource.Error("Error crítico al guardar producto: ${e.message}")
        }
    }

    suspend fun updateProducto(producto: Producto): Resource<Unit> = withContext(Dispatchers.IO) {
        try {
            productoDao.update(producto)

            try {
                val response = apiService.updateProducto(producto.id, producto.toDto())
                if (response.isSuccessful && response.body()?.success == true) {
                    Resource.Success(Unit)
                } else {
                    Resource.Error("Actualizado localmente, pero error en servidor: ${response.body()?.error}")
                }
            } catch (e: IOException) {
                Resource.Error("Actualizado localmente. Error de conexión al servidor")
            } catch (e: HttpException) {
                Resource.Error("Actualizado localmente. Error HTTP ${e.code()}: ${e.message()}")
            } catch (e: Exception) {
                Resource.Error("Actualizado localmente. Error inesperado: ${e.message}")
            }
        } catch (e: Exception) {
            Resource.Error("Error crítico al actualizar producto: ${e.message}")
        }
    }

    suspend fun deleteProducto(producto: Producto): Resource<Unit> = withContext(Dispatchers.IO) {
        try {
            productoDao.delete(producto)

            try {
                val response = apiService.deleteProducto(producto.id)
                if (response.isSuccessful && response.body()?.success == true) {
                    Resource.Success(Unit)
                } else {
                    Resource.Error("Eliminado localmente, pero error en servidor: ${response.body()?.error}")
                }
            } catch (e: IOException) {
                Resource.Error("Eliminado localmente. Error de conexión al servidor")
            } catch (e: HttpException) {
                Resource.Error("Eliminado localmente. Error HTTP ${e.code()}: ${e.message()}")
            } catch (e: Exception) {
                Resource.Error("Eliminado localmente. Error inesperado: ${e.message}")
            }
        } catch (e: Exception) {
            Resource.Error("Error crítico al eliminar producto: ${e.message}")
        }
    }

    /**
     * Sincronización manual desde el servidor
     */
    suspend fun syncProductosFromServer(): Resource<Unit> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getAllProductos()
            if (response.isSuccessful && response.body()?.success == true) {
                response.body()?.data?.let { productosDto ->
                    val productos = productosDto.map { it.toEntity() }
                    productos.forEach { productoDao.insert(it) }
                }
                Resource.Success(Unit)
            } else {
                Resource.Error("Error al sincronizar: ${response.body()?.error}")
            }
        } catch (e: IOException) {
            Resource.Error("Error de conexión: No se pudo conectar al servidor")
        } catch (e: HttpException) {
            Resource.Error("Error HTTP ${e.code()}: ${e.message()}")
        } catch (e: Exception) {
            Resource.Error("Error inesperado: ${e.message}")
        }
    }

    // === OPERACIONES DE COMPRAS ===

    fun getAllCompras(): Flow<Resource<List<Compra>>> = flow {
        emit(Resource.Loading())

        compraDao.getAll().collect { localCompras ->
            emit(Resource.Success(localCompras))
        }

        try {
            val response = apiService.getAllCompras()
            if (response.isSuccessful && response.body()?.success == true) {
                response.body()?.data?.let { comprasDto ->
                    val compras = comprasDto.map { it.toEntity() }
                    compras.forEach { compraDao.insert(it) }
                }
            } else {
                emit(Resource.Error("Error del servidor: ${response.body()?.error}"))
            }
        } catch (e: IOException) {
            emit(Resource.Error("Error de conexión: No se pudo conectar al servidor"))
        } catch (e: HttpException) {
            emit(Resource.Error("Error HTTP ${e.code()}: ${e.message()}"))
        } catch (e: Exception) {
            emit(Resource.Error("Error inesperado: ${e.message}"))
        }
    }.flowOn(Dispatchers.IO)

    fun getCompraById(id: Int): Flow<Compra?> = compraDao.getById(id)

    fun getComprasByProducto(productoId: Int): Flow<List<Compra>> =
        compraDao.getByProducto(productoId)

    fun getComprasByRangoFechas(fechaInicio: Long, fechaFin: Long): Flow<List<Compra>> =
        compraDao.getByRangoFechas(fechaInicio, fechaFin)

    fun getComprasCount(): Flow<Int> = compraDao.getCount()

    fun getTotalGastado(): Flow<Double?> = compraDao.getTotalGastado()

    suspend fun insertCompra(compra: Compra): Resource<Long> = withContext(Dispatchers.IO) {
        try {
            val producto = productoDao.getById(compra.productoId).first()
            if (producto == null) {
                return@withContext Resource.Error<Long>("Error: El producto no existe")
            }

            val localId = compraDao.insert(compra)
            val compraConId = compra.copy(id = localId.toInt())

            // ACTUALIZAR STOCK DEL PRODUCTO AUTOMÁTICAMENTE
            val productoActualizado = producto.copy(
                stockActual = producto.stockActual + compra.cantidad
            )
            productoDao.update(productoActualizado)

            try {
                val response = apiService.createCompra(compraConId.toDto())
                if (response.isSuccessful && response.body()?.success == true) {
                    response.body()?.data?.let { compraDto ->
                        val compraServidor = compraDto.toEntity()
                        if (compraServidor.id != localId.toInt()) {
                            compraDao.update(compraServidor)
                        }
                    }
                    Resource.Success(localId)
                } else {
                    Resource.Error(
                        message = "Compra guardada y stock actualizado, pero error en servidor: ${response.body()?.error}",
                        data = localId
                    )
                }
            } catch (e: IOException) {
                Resource.Error(
                    message = "Compra guardada y stock actualizado. Error de conexión al servidor",
                    data = localId
                )
            } catch (e: HttpException) {
                Resource.Error(
                    message = "Compra guardada y stock actualizado. Error HTTP ${e.code()}: ${e.message()}",
                    data = localId
                )
            } catch (e: Exception) {
                Resource.Error(
                    message = "Compra guardada y stock actualizado. Error inesperado: ${e.message}",
                    data = localId
                )
            }
        } catch (e: Exception) {
            Resource.Error("Error crítico al guardar compra: ${e.message}")
        }
    }

    suspend fun updateCompra(compra: Compra): Resource<Unit> = withContext(Dispatchers.IO) {
        try {
            compraDao.update(compra)

            try {
                val response = apiService.updateCompra(compra.id, compra.toDto())
                if (response.isSuccessful && response.body()?.success == true) {
                    Resource.Success(Unit)
                } else {
                    Resource.Error("Actualizada localmente, pero error en servidor: ${response.body()?.error}")
                }
            } catch (e: IOException) {
                Resource.Error("Actualizada localmente. Error de conexión al servidor")
            } catch (e: HttpException) {
                Resource.Error("Actualizada localmente. Error HTTP ${e.code()}: ${e.message()}")
            } catch (e: Exception) {
                Resource.Error("Actualizada localmente. Error inesperado: ${e.message}")
            }
        } catch (e: Exception) {
            Resource.Error("Error crítico al actualizar compra: ${e.message}")
        }
    }

    suspend fun deleteCompra(compra: Compra): Resource<Unit> = withContext(Dispatchers.IO) {
        try {
            compraDao.delete(compra)

            try {
                val response = apiService.deleteCompra(compra.id)
                if (response.isSuccessful && response.body()?.success == true) {
                    Resource.Success(Unit)
                } else {
                    Resource.Error("Eliminada localmente, pero error en servidor: ${response.body()?.error}")
                }
            } catch (e: IOException) {
                Resource.Error("Eliminada localmente. Error de conexión al servidor")
            } catch (e: HttpException) {
                Resource.Error("Eliminada localmente. Error HTTP ${e.code()}: ${e.message()}")
            } catch (e: Exception) {
                Resource.Error("Eliminada localmente. Error inesperado: ${e.message}")
            }
        } catch (e: Exception) {
            Resource.Error("Error crítico al eliminar compra: ${e.message}")
        }
    }

    suspend fun syncComprasFromServer(): Resource<Unit> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getAllCompras()
            if (response.isSuccessful && response.body()?.success == true) {
                response.body()?.data?.let { comprasDto ->
                    val compras = comprasDto.map { it.toEntity() }
                    compras.forEach { compraDao.insert(it) }
                }
                Resource.Success(Unit)
            } else {
                Resource.Error("Error al sincronizar: ${response.body()?.error}")
            }
        } catch (e: IOException) {
            Resource.Error("Error de conexión: No se pudo conectar al servidor")
        } catch (e: HttpException) {
            Resource.Error("Error HTTP ${e.code()}: ${e.message()}")
        } catch (e: Exception) {
            Resource.Error("Error inesperado: ${e.message}")
        }
    }

    // === OPERACIONES DE VENTAS CON VALIDACIÓN DE STOCK ===

    suspend fun insertVenta(venta: Venta): Resource<Long> = withContext(Dispatchers.IO) {
        try {
            val producto = productoDao.getById(venta.productoId).first()
            if (producto == null) {
                return@withContext Resource.Error<Long>("Error: El producto no existe")
            }

            // VALIDAR que haya stock suficiente
            if (producto.stockActual < venta.cantidad) {
                return@withContext Resource.Error<Long>(
                    "Stock insuficiente. Disponible: ${producto.stockActual}, Solicitado: ${venta.cantidad}"
                )
            }

            // VALIDAR que la cantidad sea positiva
            if (venta.cantidad <= 0) {
                return@withContext Resource.Error<Long>("La cantidad debe ser mayor a 0")
            }

            val localId = ventaDao.insert(venta)

            // ACTUALIZAR STOCK DEL PRODUCTO
            val productoActualizado = producto.copy(
                stockActual = producto.stockActual - venta.cantidad
            )
            productoDao.update(productoActualizado)

            Resource.Success(localId)

        } catch (e: Exception) {
            Resource.Error("Error crítico al registrar venta: ${e.message}")
        }
    }

    fun getAllVentas(): Flow<Resource<List<Venta>>> = flow {
        emit(Resource.Loading())
        ventaDao.getAll().collect { ventas ->
            emit(Resource.Success(ventas))
        }
    }.flowOn(Dispatchers.IO)

    fun getVentasByProducto(productoId: Int): Flow<List<Venta>> =
        ventaDao.getByProducto(productoId)

    fun getTotalVendido(): Flow<Double?> = ventaDao.getTotalVendido()
    
    fun getTotalVendidoEnRango(inicio: Long, fin: Long): Flow<Double?> =
        ventaDao.getTotalVendidoEnRango(inicio, fin)

    fun getCantidadVendidaEnRango(inicio: Long, fin: Long): Flow<Int?> =
        ventaDao.getCantidadVendidaEnRango(inicio, fin)

    fun getVentasCount(): Flow<Int> = ventaDao.getCount()

    // === FUNCIONES PARA DASHBOARD Y ESTADÍSTICAS ===

    fun getCountProductosBajoStock(): Flow<Int> =
        productoDao.getCountProductosBajoStock()

    fun getCostoTotalInventario(): Flow<Double?> =
        productoDao.getCostoTotalInventario()

    fun getTotalIngresosPorVentas(): Flow<Double?> =
        ventaDao.getTotalVendido()

    fun getGananciaNeta(): Flow<Resource<Double>> = flow {
        emit(Resource.Loading())

        try {
            // Obtener total gastado en compras
            var totalGastado = 0.0
            compraDao.getTotalGastado().collect { total ->
                totalGastado = total ?: 0.0
            }

            // Obtener total de ingresos por ventas
            var totalVendido = 0.0
            ventaDao.getTotalVendido().collect { total ->
                totalVendido = total ?: 0.0
            }

            // Calcular ganancia
            val ganancia = totalVendido - totalGastado
            emit(Resource.Success(ganancia))

        } catch (e: Exception) {
            emit(Resource.Error("Error al calcular ganancia: ${e.message}"))
        }
    }.flowOn(Dispatchers.IO)


    // Obtiene estadísticas completas para el Dashboard

    fun getEstadisticasDashboard(): Flow<Resource<DashboardStats>> = flow {
        emit(Resource.Loading())

        try {
            var totalProductos = 0
            var productosBajoStock = 0
            var costoInventario = 0.0
            var totalGastado = 0.0
            var totalVendido = 0.0
            var totalCompras = 0
            var totalVentas = 0

            // Recopilar todos los datos
            productoDao.getCount().collect { totalProductos = it }
            productoDao.getCountProductosBajoStock().collect { productosBajoStock = it }
            productoDao.getCostoTotalInventario().collect { costoInventario = it ?: 0.0 }
            compraDao.getTotalGastado().collect { totalGastado = it ?: 0.0 }
            ventaDao.getTotalVendido().collect { totalVendido = it ?: 0.0 }
            compraDao.getCount().collect { totalCompras = it }
            ventaDao.getCount().collect { totalVentas = it }

            val stats = DashboardStats(
                totalProductos = totalProductos,
                productosBajoStock = productosBajoStock,
                costoTotalInventario = costoInventario,
                totalGastadoCompras = totalGastado,
                totalIngresosVentas = totalVendido,
                gananciaNeta = totalVendido - totalGastado,
                totalCompras = totalCompras,
                totalVentas = totalVentas
            )

            emit(Resource.Success(stats))

        } catch (e: Exception) {
            emit(Resource.Error("Error al obtener estadísticas: ${e.message}"))
        }
    }.flowOn(Dispatchers.IO)
}