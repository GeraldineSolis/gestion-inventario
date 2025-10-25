package com.application.data.dao

import androidx.room.*
import com.application.model.Compra
import kotlinx.coroutines.flow.Flow

@Dao
interface CompraDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(compra: Compra): Long

    @Update
    suspend fun update(compra: Compra)

    @Delete
    suspend fun delete(compra: Compra)

    @Query("SELECT * FROM compras WHERE id = :id")
    fun getById(id: Int): Flow<Compra?>

    @Query("SELECT * FROM compras ORDER BY fechaCompra DESC")
    fun getAll(): Flow<List<Compra>>

    @Query("SELECT * FROM compras WHERE productoId = :productoId ORDER BY fechaCompra DESC")
    fun getByProducto(productoId: Int): Flow<List<Compra>>

    @Query("SELECT * FROM compras WHERE fechaCompra >= :fechaInicio AND fechaCompra <= :fechaFin ORDER BY fechaCompra DESC")
    fun getByRangoFechas(fechaInicio: Long, fechaFin: Long): Flow<List<Compra>>

    @Query("SELECT COUNT(*) FROM compras")
    fun getCount(): Flow<Int>

    @Query("SELECT SUM(cantidad * precioUnitario) FROM compras")
    fun getTotalGastado(): Flow<Double?>
}