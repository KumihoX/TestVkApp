package com.example.products.domain.repository

import androidx.paging.PagingData
import com.example.products.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun getProducts(): Flow<PagingData<Product>>
}