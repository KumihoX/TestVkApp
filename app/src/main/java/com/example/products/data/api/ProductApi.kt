package com.example.products.data.api

import com.example.products.data.api.dto.ProductsResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductApi {
    @GET("products")
    suspend fun getProducts(
        @Query("skip") skip: Int,
        @Query("limit") limit: Int
    ): ProductsResponseDto
}