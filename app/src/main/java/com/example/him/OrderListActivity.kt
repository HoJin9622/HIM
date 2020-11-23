package com.example.him

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.him.databinding.ActivityOrderListBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        // View Binding 완료. 아래부터 작성.

        showOrders(intent.getStringExtra("userId"))
    }

    private fun showOrders(userId: String?) {
        RetrofitClient.instance.getOrders(userId)
            .enqueue(object : Callback<ArrayList<OrderResponse>> {
                override fun onResponse(
                    call: Call<ArrayList<OrderResponse>>,
                    response: Response<ArrayList<OrderResponse>>
                ) {
                    // val responseCode = response.code().toString()
                    Log.d("Response", "주문 목록: ${response.body().toString()}")
                }

                override fun onFailure(call: Call<ArrayList<OrderResponse>>, t: Throwable) {
                    Log.d("Response", t.message.toString())
                }
            })
    }
}