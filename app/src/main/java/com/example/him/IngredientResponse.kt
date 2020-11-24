package com.example.him

data class IngredientResponse(
    var _id: String,
    var userId: String,
    var name: String,
    var expirationDate: String,
    var barcode: String?,
    var price: Int,
    var image: String?
)