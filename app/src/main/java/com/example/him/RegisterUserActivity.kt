package com.example.him

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.him.databinding.ActivityRegisterUserBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterUserBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        // View Binding 완료. 아래부터 작성.

        binding.signUpButton.setOnClickListener { registerHandler() }
    }

    private fun registerHandler() {
        val id = binding.idEdit.text.toString()
        val password = binding.passwordEdit.text.toString()
        val name = binding.nameEdit.text.toString()
        var isProvider = false

        when (binding.radioGroup.checkedRadioButtonId) {
            R.id.providerButton -> isProvider = true
            R.id.consumerButton -> isProvider = false
        }

        val ums = UserManagementSystem()

        ums.register(this, id, password, name, isProvider )
    }

    fun moveMainPage() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}