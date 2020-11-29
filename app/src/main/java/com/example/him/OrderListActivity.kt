package com.example.him

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.him.databinding.ActivityMainBinding
import com.example.him.databinding.ActivityOrderListBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class OrderListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        // View Binding 완료. 아래부터 작성.

        val userId = intent.getStringExtra("userId")
        if (userId == null) {
            Log.d("Response", "userId: null")
            Toast.makeText(this, "로그인 정보를 확인할 수 없습니다.", Toast.LENGTH_SHORT).show()
            moveLoginPage()
            return
        }

        OrderManagementSystem().show(this, binding, userId)
        binding.navigateMainButton.setOnClickListener { moveMainPage(userId) }
        binding.registerOrderButton.setOnClickListener { moveRegisterOrderPage(userId) }

        isProvider()
    }

    private fun isProvider() {
        RetrofitClient.instance.isProvider(intent.getStringExtra("userId"))
            .enqueue(object : Callback<IsProviderResponse> {
                override fun onResponse(
                    call: Call<IsProviderResponse>,
                    response: Response<IsProviderResponse>
                ) {
                    // val responseCode = response.code().toString()
                    Log.d("Response", "유저가 공급자인지: ${response.body().toString()}")
                }

                override fun onFailure(call: Call<IsProviderResponse>, t: Throwable) {
                    Log.d("Response", t.message.toString())
                    Toast.makeText(
                        this@OrderListActivity,
                        "서버와의 접속이 원활하지 않습니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun moveLoginPage() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun moveMainPage(userId: String) {
        startActivity(Intent(this, MainActivity::class.java).putExtra("userId", userId))
    }

    private fun moveRegisterOrderPage(userId: String) {
        startActivity(Intent(this, RegisterOrderActivity::class.java).putExtra("userId", userId))
    }
}