package com.example.products.domain

import androidx.paging.PagingData
import com.example.products.domain.model.Product
import com.example.products.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow

class GetProductsUseCase(private val productRepository: ProductRepository) {
    suspend operator fun invoke(): Flow<PagingData<Product>> = productRepository.getProducts()
}