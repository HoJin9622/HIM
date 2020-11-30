package com.example.him

import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.him.databinding.ActivityOrderListBinding
import com.example.him.databinding.ActivityRegisterOrderBinding
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
                    activity.recreate()
                    Toast.makeText(activity, "해당 주문이 취소되었습니다.", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(call: Call<MessageResponse>, t: Throwable) {
                    Log.d("Response", t.message.toString())
                    Toast.makeText(activity, "서버와의 접속이 원활하지 않습니다.", Toast.LENGTH_SHORT).show()
                }
            })
    }

    fun bringProvider(activity: AppCompatActivity, binding: ActivityRegisterOrderBinding) {
        RetrofitClient.instance.getProvider()
            .enqueue(object : Callback<java.util.ArrayList<UserResponse>> {
                override fun onResponse(
                    call: Call<java.util.ArrayList<UserResponse>>,
                    response: Response<java.util.ArrayList<UserResponse>>
                ) {
                    Log.d("Response", "공급자 목록: ${response.body()}")
                    if (response.body() == null) {
                        Toast.makeText(activity, "서버와의 접속이 원활하지 않습니다.", Toast.LENGTH_SHORT).show()
                        return
                    }

                    val providerList = response.body()!!
                    val providerNameList = mutableListOf("공급자를 선택하세요.")
                    providerNameList.addAll(providerList.map { it.name })

                    val adapter = ArrayAdapter(
                        activity, android.R.layout.simple_list_item_1, providerNameList
                    )
                    binding.providerSpinner.adapter = adapter
                    binding.providerSpinner.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>?, view: View?, position: Int, id: Long
                            ) {
                                val providerId = if (position > 0) {
                                    providerList[position - 1]._id
                                } else {
                                    null
                                }
                                showProviderIngredient(activity, binding, providerId)
                            }

                            override fun onNothingSelected(parent: AdapterView<*>?) {
                                Toast.makeText(activity, "공급자를 먼저 선택해주세요.", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                }

                override fun onFailure(
                    call: Call<java.util.ArrayList<UserResponse>>, t: Throwable
                ) {
                    Log.d("Response", t.message.toString())
                    Toast.makeText(activity, "서버와의 접속이 원활하지 않습니다.", Toast.LENGTH_SHORT).show()
                }
            })
    }

    fun showProviderIngredient(
        activity: AppCompatActivity,
        binding: ActivityRegisterOrderBinding,
        userId: String?
    ) {
        if (userId == null) {
            val adapter = ProviderIngredientAdapter(activity)
            adapter.listIngredient = emptyList()
            binding.recyclerView.adapter = adapter
            binding.recyclerView.layoutManager = LinearLayoutManager(activity)
            return
        }

        RetrofitClient.instance.getIngredients(userId)
            .enqueue(object : Callback<ArrayList<IngredientResponse>> {
                override fun onResponse(
                    call: Call<ArrayList<IngredientResponse>>,
                    response: Response<ArrayList<IngredientResponse>>
                ) {
                    Log.d("Response", "식재료 목록: ${response.body()}")
                    val ingredientList: ArrayList<IngredientResponse>? = response.body()
                    val adapter = ProviderIngredientAdapter(activity)
                    if (ingredientList != null) {
                        adapter.listIngredient = ArrayList(ingredientList)
                        binding.recyclerView.adapter = adapter
                        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
                    } else {
                        Log.d("Response", "ingredientList: null")
                    }
                }

                override fun onFailure(
                    call: Call<ArrayList<IngredientResponse>>,
                    t: Throwable
                ) {
                    Log.d("Response", t.message.toString())
                    Toast.makeText(activity, "서버와의 접속이 원활하지 않습니다.", Toast.LENGTH_SHORT).show()
                }
            })
    }

    fun orderIngredient(activity: AppCompatActivity, body: HashMap<String, String?>) {
        RetrofitClient.instance.registerOrder(body)
            .enqueue(object : Callback<MessageResponse> {
                override fun onResponse(
                    call: Call<MessageResponse>, response: Response<MessageResponse>
                ) {
                    Log.d("Response", response.toString())
                    if (response.code() == 201) {
                        Log.d("Response", "message: ${response.body()?.message}")
                        Toast.makeText(activity, "주문 목록에 추가되었습니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        Log.d("Response", "response.code(): ${response.code()}")
                        Toast.makeText(activity, "통신 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<MessageResponse>, t: Throwable) {
                    Log.d("Response", t.message.toString())
                    Toast.makeText(activity, "서버와의 접속이 원활하지 않습니다.", Toast.LENGTH_SHORT).show()
                }
            })
    }
}