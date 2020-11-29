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
        if (userId != null) {
            OrderManagementSystem().isProvider(this, binding, userId)
            OrderManagementSystem().show(this, binding, userId)
            binding.navigateMainButton.setOnClickListener { moveMainPage(userId) }
            binding.registerOrderButton.setOnClickListener { moveRegisterOrderPage(userId) }
        } else {
            Log.d("Response", "userId: null")
            Toast.makeText(this, "로그인 정보를 확인할 수 없습니다.", Toast.LENGTH_SHORT).show()
            moveLoginPage()
            finish()
        }
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