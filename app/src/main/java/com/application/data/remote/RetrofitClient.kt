package com.application.data.remote

import com.application.data.remote.api.InventarioApiService
import com.application.data.remote.mock.MockApiService

object RetrofitClient {
    private const val USE_MOCK = true

    val apiService: InventarioApiService by lazy {
        if (USE_MOCK) {
            MockApiService()
        } else {
            throw NotImplementedError("API real no configurada. Cambiar USE_MOCK a true o configurar Retrofit.")
        }
    }
}