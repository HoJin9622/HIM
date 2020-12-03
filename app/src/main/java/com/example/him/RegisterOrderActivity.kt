package com.example.him

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.him.databinding.ActivityRegisterOrderBinding

// 주문 등록 UI를 구현하는 Boundary 클래스.
class RegisterOrderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterOrderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterOrderBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        // View Binding 완료. 아래부터 작성.

        OrderManagementSystem().bringProvider(this, binding)
    }
}