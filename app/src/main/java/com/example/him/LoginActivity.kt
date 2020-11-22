package com.example.him

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.him.databinding.ActivityLoginBinding
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        // View Binding 완료. 아래부터 작성.

        signInButton.setOnClickListener { moveRegisterPage() }
        loginButton.setOnClickListener { loginHandler() }
    }

    private fun loginHandler() {
        val body = HashMap<String, String>()
        body["id"] = idEdit.text.toString()
        body["password"] = passwordEdit.text.toString()

        RetrofitClient.instance.login(body).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if(response.code().toString() == "200") {
                    Log.d("Response", response.toString())
                    Log.d("Response", "_id: " + response.body()?._id.toString())
                    Log.d("Response", "name: " + response.body()?.name.toString())
                    Log.d("Response", "id: " + response.body()?.id.toString())
                    Log.d("Response", "isProvider: " + response.body()?.isProvider.toString())
                    Toast.makeText(this@LoginActivity, "로그인 성공", Toast.LENGTH_SHORT).show()
                    moveMainPage()
                } else {
                    Toast.makeText(this@LoginActivity, "아이디 혹은 비밀번호가 올바르지 않습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.d("Response", t.message.toString())
            }
        })
    }

    fun moveRegisterPage() {
        startActivity(Intent(this, RegisterUserActivity::class.java))
        finish()
    }

    fun moveMainPage() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}