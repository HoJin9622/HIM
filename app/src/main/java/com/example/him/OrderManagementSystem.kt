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

// 주문 관리 시스템 클래스. 소비자와 공급자 간 주문 정보에 접근하는 기능을 갖는다.
class OrderManagementSystem {
    // 현재 접속한 사용자가 공급자인지 소비자인지 검사하는 함수.
    fun isProvider(activity: OrderListActivity, binding: ActivityOrderListBinding, userId: String) {
        // HTTP GET 명령으로 AWS 서버에서 접속된 사용자 ID에 해당하는 가입 정보(공급자 or 소비자)를 불러오는 함수.
        RetrofitClient.instance.isProvider(userId).enqueue(object : Callback<IsProviderResponse> {
            // HTTP GET에 대한 정상적인 응답을 수신할 때 수행하는 함수.
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
                    // 해당 사용자의 ID가 공급자일 경우 식재료 주문 버튼을 숨긴다.
                    true -> {
                        binding.registerOrderButton.hide()
                    }
                }
            }

            // HTTP GET에 대한 응답이 실패될 때 수행하는 함수.
            override fun onFailure(call: Call<IsProviderResponse>, t: Throwable) {
                Log.d("Response", t.message.toString())
                Toast.makeText(activity, "서버와의 접속이 원활하지 않습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // 현재 접속한 사용자의 식재료 주문 목록을 보여주는 합수.
    fun show(activity: OrderListActivity, binding: ActivityOrderListBinding, userId: String) {
        // HTTP GET 명령으로 AWS 서버에서 접속된 사용자 ID에 해당하는 주문 목록을 불러오는 함수.
        RetrofitClient.instance.getOrders(userId)
            .enqueue(object : Callback<ArrayList<OrderResponse>> {
                // HTTP GET에 대한 정상적인 응답을 수신할 때 수행하는 함수.
                override fun onResponse(
                    call: Call<ArrayList<OrderResponse>>,
                    response: Response<ArrayList<OrderResponse>>
                ) {
                    Log.d("Response", "주문 목록: ${response.body()}")
                    // 수신된 정보(주문 목록)을 List에 담는다.
                    val orderList = response.body()
                    // 주문 목록 UI를 동적으로 화면에 출력하기 위한 Adapter 클래스 생성.
                    val adapter = OrderAdapter(activity)
                    // 동적 UI인 Recycler View에 위 Adapter를 부착한다.
                    if (orderList != null) {
                        adapter.listOrder = ArrayList(orderList)
                        binding.recyclerView.adapter = adapter
                        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
                    } else {
                        Log.d("Response", "ingredientList: null")
                        Toast.makeText(activity, "서버와의 접속이 원활하지 않습니다.", Toast.LENGTH_SHORT).show()
                    }
                }

                // HTTP GET에 대한 응답이 실패될 때 수행하는 함수.
                override fun onFailure(call: Call<ArrayList<OrderResponse>>, t: Throwable) {
                    Log.d("Response", t.message.toString())
                    Toast.makeText(activity, "서버와의 접속이 원활하지 않습니다.", Toast.LENGTH_SHORT).show()
                }
            })
    }

    // 현재 접속 사용자의 식재료를 삭제하는 함수.
    fun delete(activity: OrderListActivity, orderId: String) {
        // HTTP DELETE 명령으로 AWS 서버에서 접속된 사용자 ID에 해당하는 주문을 삭제하는 함수.
        RetrofitClient.instance.deleteOrder(orderId)
            .enqueue(object : Callback<MessageResponse> {
                // HTTP DELETE에 대한 정상적인 응답을 수신할 때 수행하는 함수.
                override fun onResponse(
                    call: Call<MessageResponse>, response: Response<MessageResponse>
                ) {
                    Log.d("Response", "결과: $response")
                    // 주문 목록 화면에서 전체 주문 목록 UI를 갱신하여 출력하는 함수.
                    activity.showOrders()
                    Toast.makeText(activity, "해당 주문이 취소되었습니다.", Toast.LENGTH_SHORT).show()
                }

                // HTTP DELETE에 대한 응답이 실패될 때 수행하는 함수.
                override fun onFailure(call: Call<MessageResponse>, t: Throwable) {
                    Log.d("Response", t.message.toString())
                    Toast.makeText(activity, "서버와의 접속이 원활하지 않습니다.", Toast.LENGTH_SHORT).show()
                }
            })
    }

    // 식재료를 주문하기 전 공급자 목록을 불러오는 함수.
    fun bringProvider(activity: RegisterOrderActivity, binding: ActivityRegisterOrderBinding) {
        // HTTP GET 명령으로 AWS 서버에 등록된 공급자 목록을 불러오는 함수.
        RetrofitClient.instance.getProvider()
            .enqueue(object : Callback<java.util.ArrayList<UserResponse>> {
                // HTTP GET에 대한 정상적인 응답을 수신할 때 수행하는 함수.
                override fun onResponse(
                    call: Call<java.util.ArrayList<UserResponse>>,
                    response: Response<java.util.ArrayList<UserResponse>>
                ) {
                    Log.d("Response", "공급자 목록: ${response.body()}")
                    if (response.body() == null) {
                        Toast.makeText(activity, "서버와의 접속이 원활하지 않습니다.", Toast.LENGTH_SHORT).show()
                        return
                    }

                    // 수신된 정보(공급자 목록)을 List에 담는다.
                    val providerList = response.body()!!
                    // Spinner의 첫번 째 항목에는 설명을 List에 담는다.
                    val providerNameList = mutableListOf("공급자를 선택하세요.")
                    // Spinner의 나머지 항목은 공급자 정보 중 이름만 선별하여 List에 담는다.
                    providerNameList.addAll(providerList.map { it.name })

                    // 공급자 목록 UI를 동적으로 화면에 출력하기 위한 Adapter 클래스 생성.
                    val adapter = ArrayAdapter(
                        activity, android.R.layout.simple_list_item_1, providerNameList
                    )
                    binding.providerSpinner.adapter = adapter
                    binding.providerSpinner.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            // Spinner에서 공급자가 선택되었을 때 수행되는 이벤트 함수.
                            override fun onItemSelected(
                                parent: AdapterView<*>?, view: View?, position: Int, id: Long
                            ) {
                                val providerId = if (position > 0) {
                                    providerList[position - 1]._id
                                } else {
                                    null
                                }
                                // 선택된 공급자에 해당하는 식재료 목록을 불러오는 함수.
                                showProviderIngredient(activity, binding, providerId)
                            }

                            // Spinner에서 아무것도 선택되지 않았을 때 수행되는 이벤트 함수.
                            override fun onNothingSelected(parent: AdapterView<*>?) {
                                Toast.makeText(activity, "공급자를 먼저 선택해주세요.", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                }

                // HTTP GET에 대한 응답이 실패될 때 수행하는 함수.
                override fun onFailure(
                    call: Call<java.util.ArrayList<UserResponse>>, t: Throwable
                ) {
                    Log.d("Response", t.message.toString())
                    Toast.makeText(activity, "서버와의 접속이 원활하지 않습니다.", Toast.LENGTH_SHORT).show()
                }
            })
    }

    // 현재 접속한 소비자가 선택한 공급자의 식재료 목록을 불러오는 함수.
    fun showProviderIngredient(
        activity: RegisterOrderActivity,
        binding: ActivityRegisterOrderBinding,
        userId: String?
    ) {
        // 선택된 공급자의 식재료가 없을 경우 식재료 목록 UI를 비운다.
        if (userId == null) {
            val adapter = ProviderIngredientAdapter(activity)
            adapter.listIngredient = emptyList()
            binding.recyclerView.adapter = adapter
            binding.recyclerView.layoutManager = LinearLayoutManager(activity)
            return
        }

        // HTTP GET 명령으로 AWS 서버에 등록된 해당 공급자의 식재료 목록을 불러오는 함수.
        RetrofitClient.instance.getIngredients(userId)
            // HTTP GET에 대한 정상적인 응답을 수신할 때 수행하는 함수.
            .enqueue(object : Callback<ArrayList<IngredientResponse>> {
                override fun onResponse(
                    call: Call<ArrayList<IngredientResponse>>,
                    response: Response<ArrayList<IngredientResponse>>
                ) {
                    Log.d("Response", "식재료 목록: ${response.body()}")
                    // 수신된 정보(선택된 공급자의 식재료 목록)을 List에 담는다.
                    val ingredientList: ArrayList<IngredientResponse>? = response.body()
                    // 식재료 목록 UI를 동적으로 화면에 출력하기 위한 Adapter 클래스 생성.
                    val adapter = ProviderIngredientAdapter(activity)
                    if (ingredientList != null) {
                        // 식재료 목록 UI를 갱신한다.
                        adapter.listIngredient = ArrayList(ingredientList)
                        binding.recyclerView.adapter = adapter
                        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
                    } else {
                        Log.d("Response", "ingredientList: null")
                    }
                }

                // HTTP GET에 대한 응답이 실패될 때 수행하는 함수.
                override fun onFailure(
                    call: Call<ArrayList<IngredientResponse>>,
                    t: Throwable
                ) {
                    Log.d("Response", t.message.toString())
                    Toast.makeText(activity, "서버와의 접속이 원활하지 않습니다.", Toast.LENGTH_SHORT).show()
                }
            })
    }

    // 소비자가 선택한 공급자의 식재료 주문을 수행하는 함수.
    fun orderIngredient(activity: RegisterOrderActivity, body: HashMap<String, String?>) {
        // HTTP 통신 중 POST 명령을 수행하여 식재료 주문 목록을 등록한다.
        RetrofitClient.instance.registerOrder(body)
            .enqueue(object : Callback<MessageResponse> {
                // HTTP POST에 대한 정상적인 응답을 수실할 때 수행하는 함수.
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

                // HTTP POST에 대한 응답이 실패할 때 수행하는 함수.
                override fun onFailure(call: Call<MessageResponse>, t: Throwable) {
                    Log.d("Response", t.message.toString())
                    Toast.makeText(activity, "서버와의 접속이 원활하지 않습니다.", Toast.LENGTH_SHORT).show()
                }
            })
    }
}