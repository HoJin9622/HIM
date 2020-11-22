package com.example.him

import retrofit2.Call
import retrofit2.http.*

interface Api {
    @GET("ingredients/{userId}")
    fun getIngredients(@Path("userId") userId: String): Call<ArrayList<IngredientResponse>>

    @POST("users/login")
    fun login(
        @Body params: HashMap<String, String>
    ): Call<UserResponse>

    @POST("users")
    fun register(
        @Body params: HashMap<String, Any>
    ): Call<UserResponse>
}