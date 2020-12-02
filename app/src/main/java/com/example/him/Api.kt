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

    @GET("users/provider")
    fun getProvider(): Call<ArrayList<UserResponse>>

    @GET("users/isProvider/{id}")
    fun isProvider(@Path("id") id: String?): Call<IsProviderResponse>

    @PUT("users/profile")
    fun editUser(
        @Body params: HashMap<String, String?>
    ): Call<UserResponse>

    @DELETE("users/{id}")
    fun deleteUser(@Path("id") id: String?): Call<MessageResponse>

    @GET("ingredients/{userId}")
    fun getIngredients(@Path("userId") userId: String?): Call<ArrayList<IngredientResponse>>

    @GET("ingredients/barcode/{id}")
    fun getIngredient(@Path("id") id: String?): Call<IngredientResponse>

    @POST("ingredients")
    fun registerIngredient(
        @Body params: HashMap<String, Any?>
    ): Call<IngredientResponse>

    @PUT("ingredients")
    fun editIngredient(
        @Body params: HashMap<String, Any?>
    ): Call<IngredientResponse>

    @DELETE("ingredients/{id}")
    fun deleteIngredients(@Path("id") id: String?): Call<MessageResponse>

    @GET("orders/myorders/{userId}")
    fun getOrders(@Path("userId") userId: String?): Call<ArrayList<OrderResponse>>

    @POST("orders")
    fun registerOrder(
        @Body params: HashMap<String, String?>
    ): Call<MessageResponse>

    @DELETE("orders/{id}")
    fun deleteOrder(@Path("id") id: String?): Call<MessageResponse>
}