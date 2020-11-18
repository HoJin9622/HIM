package com.example.him

data class IngredientResponse(
    var _id: String,
    var user: String,
    var name: String,
    var image: String?,
    var category: String,
    var memo: String,
    var barcode: String,
    var __v: Int
)