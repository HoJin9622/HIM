package com.example.him

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.him.databinding.ActivityOrderListBinding

class OrderListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        // View Binding 완료. 아래부터 작성.

        val userId = intent.getStringExtra("userId") ?: return
        showOrders(userId)
        binding.navigateMainButton.setOnClickListener { moveMainPage(userId) }
        binding.registerOrderButton.setOnClickListener { moveRegisterOrderPage(userId) }
    }

    private fun showOrders(userId: String) {
        OrderManagementSystem().list(this, userId)
    }

    private fun moveMainPage(userId: String) {
        startActivity(Intent(this, MainActivity::class.java).putExtra("userId", userId))
    }

    private fun moveRegisterOrderPage(userId: String) {
        OrderManagementSystem().page(this, userId)
    }
}