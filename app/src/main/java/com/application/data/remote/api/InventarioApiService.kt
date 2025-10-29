package com.application.data.remote.api

import com.application.data.remote.dto.ApiResponse
import com.application.data.remote.dto.CompraDto
import com.application.data.remote.dto.ProductoDto
import retrofit2.Response
import retrofit2.http.*

interface InventarioApiService {
    @GET("productos")
    suspend fun getAllProductos(): Response<ApiResponse<List<ProductoDto>>>

    @GET("productos/{id}")
    suspend fun getProductoById(@Path("id") id: Int): Response<ApiResponse<ProductoDto>>

    @POST("productos")
    suspend fun createProducto(@Body producto: ProductoDto): Response<ApiResponse<ProductoDto>>

    @PUT("productos/{id}")
    suspend fun updateProducto(
        @Path("id") id: Int,
        @Body producto: ProductoDto
    ): Response<ApiResponse<ProductoDto>>

    @DELETE("productos/{id}")
    suspend fun deleteProducto(@Path("id") id: Int): Response<ApiResponse<Unit>>

    @GET("productos/categoria/{categoria}")
    suspend fun getProductosByCategoria(
        @Path("categoria") categoria: String
    ): Response<ApiResponse<List<ProductoDto>>>

    @GET("productos/bajo-stock")
    suspend fun getProductosBajoStock(): Response<ApiResponse<List<ProductoDto>>>

    // === ENDPOINTS DE COMPRAS ===

    @GET("compras")
    suspend fun getAllCompras(): Response<ApiResponse<List<CompraDto>>>

    @GET("compras/{id}")
    suspend fun getCompraById(@Path("id") id: Int): Response<ApiResponse<CompraDto>>

    @POST("compras")
    suspend fun createCompra(@Body compra: CompraDto): Response<ApiResponse<CompraDto>>

    @PUT("compras/{id}")
    suspend fun updateCompra(
        @Path("id") id: Int,
        @Body compra: CompraDto
    ): Response<ApiResponse<CompraDto>>

    @DELETE("compras/{id}")
    suspend fun deleteCompra(@Path("id") id: Int): Response<ApiResponse<Unit>>

    @GET("compras/producto/{productoId}")
    suspend fun getComprasByProducto(
        @Path("productoId") productoId: Int
    ): Response<ApiResponse<List<CompraDto>>>
}