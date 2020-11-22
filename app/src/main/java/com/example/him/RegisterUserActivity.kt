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
        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.providerButton -> Log.d("isProvider", "true")
                R.id.consumerButton -> Log.d("isProvider", "false")
            }
        }
    }

    private fun registerHandler() {
        val body = HashMap<String, Any>()
        body["id"] = binding.idEdit.text.toString()
        body["password"] = binding.passwordEdit.text.toString()
        body["name"] = binding.nameEdit.text.toString()
        body["isProvider"] = false
        Log.d("isProvider", binding.radioGroup.checkedRadioButtonId.toString())

        RetrofitClient.instance.register(body).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                Log.d("Response", response.toString())
                if (response.code() == 201) {
                    Log.d("Response", "_id: " + response.body()?._id.toString())
                    Log.d("Response", "name: " + response.body()?.name.toString())
                    Log.d("Response", "id: " + response.body()?.id.toString())
                    Log.d("Response", "isProvider: " + response.body()?.isProvider.toString())
                    Toast.makeText(this@RegisterUserActivity, "회원가입 성공", Toast.LENGTH_SHORT).show()
                    moveMainPage()
                } else {
                    Toast.makeText(
                        this@RegisterUserActivity,
                        "이미 존재하는 아이디입니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.d("Response", t.message.toString())
            }
        })
    }

    fun moveMainPage() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}