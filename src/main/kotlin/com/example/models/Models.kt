package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int,
    val name: String,
    val email: String,
    val status: String
)

@Serializable
data class Product(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val category: String,
    val stockQuantity: Int
)

@Serializable
data class CreateUserRequest(
    val name: String,
    val email: String
)

@Serializable
data class UpdateUserRequest(
    val name: String,
    val email: String
)

@Serializable
data class CreateProductRequest(
    val name: String,
    val description: String,
    val price: Double,
    val category: String,
    val stockQuantity: Int
)

@Serializable
data class UpdateProductRequest(
    val name: String,
    val description: String,
    val price: Double,
    val category: String,
    val stockQuantity: Int
)

@Serializable
data class UsersResponse(
    val users: List<User>
)

@Serializable
data class ProductsResponse(
    val products: List<Product>
)

@Serializable
data class ErrorResponse(
    val error: String
) 