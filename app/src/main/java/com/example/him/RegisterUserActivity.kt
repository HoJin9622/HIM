package com.example.him

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.him.databinding.ActivityRegisterUserBinding

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
        var isProvider: Boolean? = null
        when (binding.radioGroup.checkedRadioButtonId) {
            R.id.providerButton -> isProvider = true
            R.id.consumerButton -> isProvider = false
        }

        when {
            id.isEmpty() -> {
                Toast.makeText(this, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show()
                return
            }
            password.isEmpty() -> {
                Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                return
            }
            name.isEmpty() -> {
                Toast.makeText(this, "이름을 입력해주세요.", Toast.LENGTH_SHORT).show()
                return
            }
            isProvider == null -> {
                Toast.makeText(this, "가입 유형을 선택해주세요.", Toast.LENGTH_SHORT).show()
                return
            }
        }
        UserManagementSystem().register(this, id, password, name, isProvider!!)
    }
}