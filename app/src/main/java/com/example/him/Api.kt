package com.example.him

import retrofit2.Call
import retrofit2.http.*

interface Api {
    @POST("users/login")
    fun login(
        @Body params: HashMap<String, String>
    ): Call<UserResponse>

    @POST("users")
    fun register(
        @Body params: HashMap<String, Any>
    ): Call<UserResponse>

    @PUT("users/profile")
    fun editUser(
        @Body params: HashMap<String, String?>
    ): Call<UserResponse>

    @DELETE("users/{id}")
    fun deleteUser(@Path("id") id: String?): Call<MessageResponse>

    @GET("ingredients/{userId}")
    fun getIngredients(@Path("userId") userId: String?): Call<ArrayList<IngredientResponse>>

    @POST("ingredients")
    fun registerIngredient(
        @Body params: HashMap<String, Any?>
    ): Call<IngredientResponse>

    @GET("orders/myorders/{userId}")
    fun getOrders(@Path("userId") userId: String?): Call<ArrayList<OrderResponse>>
}