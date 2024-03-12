package com.example.products

import com.example.products.common.Constants
import com.example.products.data.api.ProductApi
import com.example.products.data.repository.ProductRepositoryImpl
import com.example.products.domain.GetProductsUseCase
import com.example.products.domain.repository.ProductRepository
import com.example.products.ui.MainViewModel
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val appModule = module {

    factory { provideRetrofit() }
    single { provideProductApi(get()) }

    single<ProductRepository> { ProductRepositoryImpl(api = get()) }

    single<GetProductsUseCase> { GetProductsUseCase(productRepository = get()) }

    viewModel {
        MainViewModel(getProductsUseCase = get())
    }
}

fun provideRetrofit(): Retrofit {
    return Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(
            OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build()
        )
        .build()

}

fun provideProductApi(retrofit: Retrofit): ProductApi =
    retrofit.create(ProductApi::class.java)
