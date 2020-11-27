package com.example.him

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class IngredientManagementSystem {

    fun register(
        activity: AppCompatActivity,
        userId: String,
        image: String?,
        barcode: String?,
        name: String,
        expirationDate: String,
        price: Int
    ) {
        val body = HashMap<String, Any?>()
        body["user"] = userId
        body["image"] = image
        body["barcode"] = barcode
        body["name"] = name
        body["expirationDate"] = expirationDate
        body["price"] = price

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
                            Intent(activity, MainActivity::class.java).putExtra("userId", userId)
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
}