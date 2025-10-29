package com.application.data

import android.content.Context
import androidx.room.*
import com.application.data.dao.CompraDao
import com.application.data.dao.ProductoDao
import com.application.data.dao.VentaDao
import com.application.model.Compra
import com.application.model.Producto
import com.application.model.Venta

@Database(
    entities = [Producto::class, Compra::class, Venta::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun productoDao(): ProductoDao
    abstract fun compraDao(): CompraDao
    abstract fun ventaDao(): VentaDao


    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "inventario_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}