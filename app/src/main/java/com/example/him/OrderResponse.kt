package com.example.him

// 주문 Entity 클래스
data class OrderResponse (
    val _id: String,  // Primary Key
    val buyer: UserResponse,  // 소비자(구매자)
    val seller: UserResponse,  // 공급자(판매자)
    val orderIngredient: IngredientResponse // 주문 식재료
)