package com.application.data.repository

import com.application.data.dao.CompraDao
import com.application.data.dao.ProductoDao
import com.application.data.remote.RetrofitClient
import com.application.data.remote.dto.toDto
import com.application.data.remote.dto.toEntity
import com.application.model.Compra
import com.application.model.Producto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
class InventoryRepository(
    private val productoDao: ProductoDao,
    private val compraDao: CompraDao
) {

    private val apiService = RetrofitClient.apiService

    // OPERACIONES DE PRODUCTOS

    fun getAllProductos(): Flow<Resource<List<Producto>>> = flow {
        emit(Resource.Loading())

        productoDao.getAll().collect { localProductos ->
            emit(Resource.Success(localProductos))
        }

        try {
            val response = apiService.getAllProductos()
            if (response.isSuccessful && response.body()?.success == true) {
                response.body()?.data?.let { productosDto ->
                    val productos = productosDto.map { it.toEntity() }
                    productos.forEach { productoDao.insert(it) }
                }
            }
        } catch (e: Exception) {
            emit(Resource.Error("Error de red: ${e.message}"))
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
                }
            } catch (e: Exception) {
                // Marcar como pendiente de sincronización (opcional)
                // En una implementación real, guardarías esto para sincronizar después
            }

            Resource.Success(localId)
        } catch (e: Exception) {
            Resource.Error("Error al insertar producto: ${e.message}")
        }
    }

    suspend fun updateProducto(producto: Producto): Resource<Unit> = withContext(Dispatchers.IO) {
        try {
            productoDao.update(producto)

            try {
                apiService.updateProducto(producto.id, producto.toDto())
            } catch (e: Exception) {
                // Marcar como pendiente de sincronización
            }

            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error("Error al actualizar producto: ${e.message}")
        }
    }

    suspend fun deleteProducto(producto: Producto): Resource<Unit> = withContext(Dispatchers.IO) {
        try {
            productoDao.delete(producto)

            try {
                apiService.deleteProducto(producto.id)
            } catch (e: Exception) {
                // Marcar como pendiente de sincronización
            }

            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error("Error al eliminar producto: ${e.message}")
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
        } catch (e: Exception) {
            Resource.Error("Error de conexión: ${e.message}")
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
            }
        } catch (e: Exception) {
            emit(Resource.Error("Error de red: ${e.message}"))
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
            val localId = compraDao.insert(compra)

            try {
                val response = apiService.createCompra(compra.toDto())
                if (response.isSuccessful && response.body()?.success == true) {
                    response.body()?.data?.let { compraDto ->
                        compraDao.update(compraDto.toEntity())
                    }
                }
            } catch (e: Exception) {
                // Marcar para sincronización posterior
            }

            Resource.Success(localId)
        } catch (e: Exception) {
            Resource.Error("Error al insertar compra: ${e.message}")
        }
    }

    suspend fun updateCompra(compra: Compra): Resource<Unit> = withContext(Dispatchers.IO) {
        try {
            compraDao.update(compra)

            try {
                apiService.updateCompra(compra.id, compra.toDto())
            } catch (e: Exception) {
                // Marcar para sincronización
            }

            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error("Error al actualizar compra: ${e.message}")
        }
    }

    suspend fun deleteCompra(compra: Compra): Resource<Unit> = withContext(Dispatchers.IO) {
        try {
            compraDao.delete(compra)

            try {
                apiService.deleteCompra(compra.id)
            } catch (e: Exception) {
                // Marcar para sincronización
            }

            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error("Error al eliminar compra: ${e.message}")
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
        } catch (e: Exception) {
            Resource.Error("Error de conexión: ${e.message}")
        }
    }
}