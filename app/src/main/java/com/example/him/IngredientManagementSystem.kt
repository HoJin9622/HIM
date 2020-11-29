package com.example.him

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class IngredientManagementSystem {
    fun register(activity: AppCompatActivity, body: HashMap<String, Any?>) {
        RetrofitClient.instance.registerIngredient(body)
            .enqueue(object : Callback<IngredientResponse> {
                override fun onResponse(
                    call: Call<IngredientResponse>,
                    response: Response<IngredientResponse>
                ) {
                    Log.d("Response", response.toString())
                    if (response.code() == 201) {
                        Log.d("Response", "_id: ${response.body()?._id}")
                        Log.d("Response", "user: ${response.body()?.user}")
                        Log.d("Response", "name: ${response.body()?.name}")
                        Log.d("Response", "expirationDate: ${response.body()?.expirationDate}")
                        Log.d("Response", "image: ${response.body()?.image}")
                        Log.d("Response", "barcode: ${response.body()?.barcode}")
                        activity.startActivity(
                            Intent(
                                activity,
                                MainActivity::class.java
                            ).putExtra("userId", body["user"].toString())
                        )
                        activity.finish()
                    } else {
                        Log.d("Response", "response.code(): ${response.code()}")
                        Toast.makeText(activity, "통신 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<IngredientResponse>, t: Throwable) {
                    Log.d("Response", t.message.toString())
                    Toast.makeText(activity, "서버와의 접속이 원활하지 않습니다.", Toast.LENGTH_SHORT).show()
                }
            })
    }

    fun edit(activity: AppCompatActivity, ingredientId: String) {
        val userId = activity.intent.getStringExtra("userId")
        activity.startActivity(
            Intent(activity, IngredientActivity::class.java).putExtra("userId", userId)
                .putExtra("ingredientId", ingredientId)
        )
    }

    fun delete(activity: AppCompatActivity, ingredientId: String) {
        RetrofitClient.instance.deleteIngredients(ingredientId)
            .enqueue(object : Callback<MessageResponse> {
                override fun onResponse(
                    call: Call<MessageResponse>,
                    response: Response<MessageResponse>
                ) {
                    Log.d("Response", "결과: $response")

                    val userId = activity.intent.getStringExtra("userId")
                    activity.finish()
                    activity.startActivity(
                        Intent(
                            activity,
                            MainActivity::class.java
                        ).putExtra("userId", userId)
                    )
                    Toast.makeText(activity, "해당 식재료가 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(call: Call<MessageResponse>, t: Throwable) {
                    Log.d("Response", t.message.toString())
                    Toast.makeText(activity, "서버와의 접속이 원활하지 않습니다.", Toast.LENGTH_SHORT).show()
                }
            })
    }
}