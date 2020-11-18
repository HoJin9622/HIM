package com.example.him

import retrofit2.Call
import retrofit2.http.*

interface Api {
    @GET("ingredients/5fb1016596cb2c13c73d0801")
    fun getIngredients(): Call<ArrayList<IngredientResponse>>

    @POST("users/login")
    fun login(
        @Body params: HashMap<String, String>
    ): Call<LoginResponse>
}