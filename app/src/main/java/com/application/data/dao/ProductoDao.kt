package com.application.data.dao

import androidx.room.*
import com.application.model.Producto
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(producto: Producto): Long

    @Update
    suspend fun update(producto: Producto)

    @Delete
    suspend fun delete(producto: Producto)

    @Query("SELECT * FROM productos WHERE id = :id")
    fun getById(id: Int): Flow<Producto?>

    @Query("SELECT * FROM productos ORDER BY nombre ASC")
    fun getAll(): Flow<List<Producto>>

    @Query("SELECT * FROM productos WHERE categoria = :categoria ORDER BY nombre ASC")
    fun getByCategoria(categoria: String): Flow<List<Producto>>

    @Query("SELECT * FROM productos WHERE stockActual <= stockMinimo")
    fun getProductosBajoStock(): Flow<List<Producto>>

    @Query("SELECT COUNT(*) FROM productos")
    fun getCount(): Flow<Int>
}