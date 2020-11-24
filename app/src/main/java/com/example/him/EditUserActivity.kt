package com.example.him

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.him.databinding.ActivityEditUserBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditUserBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        // View Binding 완료. 아래부터 작성.

        binding.withdrawButton.setOnClickListener { withdrawHandler(intent.getStringExtra("userId")) }
        binding.modifyButton.setOnClickListener { userEditHandler() }
    }

    private fun withdrawHandler(userId: String?) {
        val ums = UserManagementSystem()

        if (userId != null) {
            ums.withdraw(this, userId)
        }
    }

    private fun userEditHandler() {

        val id = intent.getStringExtra("userId")
        val password = binding.passwordEdit.text.toString()

        val ums = UserManagementSystem()

        if (id != null) {
            ums.edit(this, id, password)

        }
    }
}