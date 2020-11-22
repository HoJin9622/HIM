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
                Log.d("Response", response.toString())
                Log.d("Response", "_id: " + response.body()?._id.toString())
                Log.d("Response", "name: " + response.body()?.name.toString())
                Log.d("Response", "id: " + response.body()?.id.toString())
                Log.d("Response", "isProvider: " + response.body()?.isProvider.toString())

                moveMainPage()
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.d("Response", t.message.toString())
            }
        })
    }

    fun moveRegisterPage() {
        startActivity(Intent(this, RegisterUserActivity::class.java))
    }

    fun moveMainPage() {
        startActivity(Intent(this, MainActivity::class.java))
    }
}