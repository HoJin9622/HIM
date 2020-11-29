package com.example.him

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.him.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.HashMap

class IngredientManagementSystem {
    private lateinit var binding: ActivityMainBinding

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

    fun edit(
        activity: AppCompatActivity,
        ingredientId: String,
        name: String,
        price: String,
        barcode: String?,
        expirationDate: Date
    ) {
        val userId = activity.intent.getStringExtra("userId")
        activity.startActivity(
            Intent(activity, IngredientActivity::class.java).putExtra("userId", userId)
                .putExtra("ingredientId", ingredientId).putExtra("name", name)
                .putExtra("price", price).putExtra("barcode", barcode)
                .putExtra("expirationDate", expirationDate.toString())
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

    fun show(activity: AppCompatActivity, userId: String?) {
        RetrofitClient.instance.getIngredients(userId)
            .enqueue(object : Callback<ArrayList<IngredientResponse>> {
                override fun onResponse(
                    call: Call<ArrayList<IngredientResponse>>,
                    response: Response<ArrayList<IngredientResponse>>
                ) {
                    // val responseCode = response.code().toString()
                    Log.d("Response", "식재료 목록: " + response.body().toString())

                    val ingredientList: ArrayList<IngredientResponse>? = response.body()
                    val adapter = IngredientAdapter(activity)
                    if (ingredientList != null) {
                        adapter.listIngredient = ingredientList
                        binding.recyclerView.adapter = adapter
                        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
                    } else {
                        Log.d("Response", "ingredientList: null")
                    }
                }

                override fun onFailure(call: Call<ArrayList<IngredientResponse>>, t: Throwable) {
                    Log.d("Response", t.message.toString())
                    Toast.makeText(activity, "서버와의 접속이 원활하지 않습니다.", Toast.LENGTH_SHORT).show()
                }
            })
    }
}