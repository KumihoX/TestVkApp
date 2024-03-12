package com.example.products.data.api.dto

import com.example.products.domain.model.Product

data class ProductDto(
    val id: String,
    val title: String,
    val description: String,
    val price: Int,
    val discountPercentage: Float,
    val rating: Float,
    val stock: Int,
    val brand: String,
    val category: String,
    val thumbnail: String,
    val images: List<String>
)

fun ProductDto.toProduct() = Product(
    id = this.id,
    title = this.title,
    description = this.description,
    thumbnail = this.thumbnail
)
