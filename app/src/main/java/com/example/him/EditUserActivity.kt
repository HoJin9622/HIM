package com.example.him

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.him.databinding.ActivityEditUserBinding

class EditUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditUserBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        // View Binding 완료. 아래부터 작성.

        binding.withdrawButton.setOnClickListener { withdrawHandler(intent.getStringExtra("userId")) }
        binding.modifyButton.setOnClickListener { editHandler(intent.getStringExtra("userId")) }
    }

    private fun withdrawHandler(userId: String?) {
        if (userId != null) {
            UserManagementSystem().withdraw(this, userId)
        }
    }

    private fun editHandler(userId: String?) {
        val password = binding.passwordEdit.text.toString()
        val passwordCheck = binding.passwordCheck.text.toString()
        when {
            password.isEmpty() -> {
                Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                return
            }
            password != passwordCheck -> {
                Toast.makeText(this, "비밀번호가 서로 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            }
            userId != null -> {
                UserManagementSystem().edit(this, userId, password)
            }
        }
    }
}