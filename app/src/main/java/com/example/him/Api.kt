package com.example.him

import retrofit2.Call
import retrofit2.http.GET

interface Api {
    @GET("ingredients/5fb1016596cb2c13c73d0801")
    fun getIngredients(): Call<ArrayList<IngredientResponse>>
}