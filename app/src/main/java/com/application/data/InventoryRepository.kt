package com.application.data

import com.application.data.dao.CompraDao
import com.application.data.dao.ProductoDao
import com.application.model.Compra
import com.application.model.Producto
import kotlinx.coroutines.flow.Flow

class InventoryRepository(
    private val productoDao: ProductoDao,
    private val compraDao: CompraDao
) {

    // OPERACIONES DE PRODUCTOS

    fun getAllProductos(): Flow<List<Producto>> = productoDao.getAll()

    fun getProductoById(id: Int): Flow<Producto?> = productoDao.getById(id)

    fun getProductosByCategoria(categoria: String): Flow<List<Producto>> =
        productoDao.getByCategoria(categoria)

    fun getProductosBajoStock(): Flow<List<Producto>> =
        productoDao.getProductosBajoStock()

    fun getProductosCount(): Flow<Int> = productoDao.getCount()

    suspend fun insertProducto(producto: Producto): Long =
        productoDao.insert(producto)

    suspend fun updateProducto(producto: Producto) =
        productoDao.update(producto)

    suspend fun deleteProducto(producto: Producto) =
        productoDao.delete(producto)

    // OPERACIONES DE COMPRAS

    fun getAllCompras(): Flow<List<Compra>> = compraDao.getAll()

    fun getCompraById(id: Int): Flow<Compra?> = compraDao.getById(id)

    fun getComprasByProducto(productoId: Int): Flow<List<Compra>> =
        compraDao.getByProducto(productoId)

    fun getComprasByRangoFechas(fechaInicio: Long, fechaFin: Long): Flow<List<Compra>> =
        compraDao.getByRangoFechas(fechaInicio, fechaFin)

    fun getComprasCount(): Flow<Int> = compraDao.getCount()

    fun getTotalGastado(): Flow<Double?> = compraDao.getTotalGastado()

    suspend fun insertCompra(compra: Compra): Long =
        compraDao.insert(compra)

    suspend fun updateCompra(compra: Compra) =
        compraDao.update(compra)

    suspend fun deleteCompra(compra: Compra) =
        compraDao.delete(compra)
}