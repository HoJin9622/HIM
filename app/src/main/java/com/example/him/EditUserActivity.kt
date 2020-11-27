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
        val ums = UserManagementSystem()
        if (userId != null) {
            ums.withdraw(this, userId)
        }
    }

    private fun editHandler(userId: String?) {
        if (binding.passwordEdit.text.toString() == binding.passwordCheck.text.toString()) {
            val password = binding.passwordEdit.text.toString()
            if (userId != null) {
                UserManagementSystem().edit(this, userId, password)
            }
        } else {
            Toast.makeText(this, "비밀번호가 서로 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            binding.passwordEdit.setText("")
            binding.passwordCheck.setText("")
        }
    }
}