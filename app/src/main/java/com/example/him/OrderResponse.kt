package com.example.him

data class OrderResponse (
    val _id: String,
    val buyer: UserResponse,
    val seller: UserResponse,
    val orderIngredient: IngredientResponse
)