package com.example.him

import java.util.*

data class IngredientResponse(
    var _id: String,
    var user: String,
    var barcode: String?,
    var name: String,
    var expirationDate: Date,
    var category: String,
    var image: String?,
    var memo: String?
)