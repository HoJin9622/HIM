package com.example.him

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.him.databinding.ActivityOrderListBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderManagementSystem {
    fun isProvider(activity: AppCompatActivity, binding: ActivityOrderListBinding, userId: String) {
        RetrofitClient.instance.isProvider(userId).enqueue(object : Callback<IsProviderResponse> {
            override fun onResponse(
                call: Call<IsProviderResponse>,
                response: Response<IsProviderResponse>
            ) {
                val isProvider = response.body()?.isProvider
                Log.d("Response", "유저가 공급자인지: $isProvider")
                when (isProvider) {
                    null -> {
                        Toast.makeText(activity, "서버와의 접속이 원활하지 않습니다.", Toast.LENGTH_SHORT).show()
                    }
                    true -> {
                        binding.registerOrderButton.hide()
                    }
                }
            }

            override fun onFailure(call: Call<IsProviderResponse>, t: Throwable) {
                Log.d("Response", t.message.toString())
                Toast.makeText(activity, "서버와의 접속이 원활하지 않습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun show(activity: AppCompatActivity, binding: ActivityOrderListBinding, userId: String) {
        RetrofitClient.instance.getOrders(userId)
            .enqueue(object : Callback<ArrayList<OrderResponse>> {
                override fun onResponse(
                    call: Call<ArrayList<OrderResponse>>,
                    response: Response<ArrayList<OrderResponse>>
                ) {
                    Log.d("Response", "주문 목록: ${response.body()}")
                    val orderList = response.body()
                    val adapter = OrderAdapter(activity)
                    if (orderList != null) {
                        adapter.listOrder = ArrayList(orderList)
                        binding.recyclerView.adapter = adapter
                        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
                    } else {
                        Log.d("Response", "ingredientList: null")
                        Toast.makeText(activity, "서버와의 접속이 원활하지 않습니다.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ArrayList<OrderResponse>>, t: Throwable) {
                    Log.d("Response", t.message.toString())
                    Toast.makeText(activity, "서버와의 접속이 원활하지 않습니다.", Toast.LENGTH_SHORT).show()
                }
            })
    }

    fun delete(activity: AppCompatActivity, orderId: String) {
        RetrofitClient.instance.deleteOrder(orderId)
            .enqueue(object : Callback<MessageResponse> {
                override fun onResponse(
                    call: Call<MessageResponse>, response: Response<MessageResponse>
                ) {
                    Log.d("Response", "결과: $response")
                    val userId = activity.intent.getStringExtra("userId")
                    activity.finish()
                    activity.startActivity(
                        Intent(activity, OrderListActivity::class.java).putExtra("userId", userId)
                    )
                    Toast.makeText(activity, "해당 주문이 취소되었습니다.", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(call: Call<MessageResponse>, t: Throwable) {
                    Log.d("Response", t.message.toString())
                    Toast.makeText(activity, "서버와의 접속이 원활하지 않습니다.", Toast.LENGTH_SHORT).show()
                }
            })
    }
}