package com.example.products.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.products.common.Constants.PAGE_PRODUCT_LIMIT
import com.example.products.data.ProductsPagingSource
import com.example.products.data.api.ProductApi
import com.example.products.domain.model.Product
import com.example.products.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow

class ProductRepositoryImpl(
    private val api: ProductApi,
) : ProductRepository {

    override suspend fun getProducts(): Flow<PagingData<Product>> {
        return Pager(
            config = PagingConfig(pageSize = PAGE_PRODUCT_LIMIT),
            pagingSourceFactory = { ProductsPagingSource(api) }
        ).flow
    }
}

