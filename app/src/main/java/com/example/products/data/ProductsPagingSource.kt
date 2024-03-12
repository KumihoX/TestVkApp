package com.example.products.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.products.common.Constants.PAGE_PRODUCT_LIMIT
import com.example.products.data.api.ProductApi
import com.example.products.data.api.dto.toProduct
import com.example.products.domain.model.Product
import retrofit2.HttpException
import java.io.IOException

class ProductsPagingSource(
    private val api: ProductApi,
) : PagingSource<Int, Product>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        return try {
            val currentPage = params.key ?: 1
            val products = api.getProducts(
                skip = (currentPage - 1) * 20,
                limit = PAGE_PRODUCT_LIMIT
            )
            LoadResult.Page(
                data = products.products.map { it.toProduct() },
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = if (products.products.isEmpty()) null else currentPage.plus(1)
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return state.anchorPosition
    }
}