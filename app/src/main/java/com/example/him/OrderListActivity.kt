package com.example.him

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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

        binding.registerOrderButton.setOnClickListener{ moveRegisterOrderPage(intent.getStringExtra("userId")) }
    }

    private fun showOrders(userId: String?) {
        val show = OrderManagementSystem()
        if (userId != null) {
            show.list(this, userId)
        }
    }

    private fun moveRegisterOrderPage(_id: String?) {
        startActivity(Intent(this, RegisterOrderActivity::class.java).putExtra("userId", _id))
    }
}