package com.application.data.dao

import androidx.room.*
import com.application.model.Venta
import kotlinx.coroutines.flow.Flow

@Dao
interface VentaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(venta: Venta): Long

    @Update
    suspend fun update(venta: Venta)

    @Delete
    suspend fun delete(venta: Venta)

    @Query("SELECT * FROM ventas WHERE id = :id")
    fun getById(id: Int): Flow<Venta?>

    @Query("SELECT * FROM ventas ORDER BY fechaVenta DESC")
    fun getAll(): Flow<List<Venta>>

    @Query("SELECT * FROM ventas WHERE productoId = :productoId ORDER BY fechaVenta DESC")
    fun getByProducto(productoId: Int): Flow<List<Venta>>

    @Query("SELECT * FROM ventas WHERE fechaVenta >= :fechaInicio AND fechaVenta <= :fechaFin ORDER BY fechaVenta DESC")
    fun getByRangoFechas(fechaInicio: Long, fechaFin: Long): Flow<List<Venta>>

    @Query("SELECT COUNT(*) FROM ventas")
    fun getCount(): Flow<Int>

    @Query("SELECT SUM(cantidad * precioUnitario) FROM ventas")
    fun getTotalVendido(): Flow<Double?>
}