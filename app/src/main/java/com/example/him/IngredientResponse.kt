package com.example.him

import java.io.Serializable
import java.util.*

// 식재료 Entity 클래스
data class IngredientResponse(
    var _id: String,  // Primary Key
    var user: String,  // 회원 아이디
    var barcode: String?,  // 바코드
    var name: String,  // 식재료 이름
    var expirationDate: Date,  // 유통기한
    var image: String?,  // 사진
    var price: Int  // 가격
) : Serializable