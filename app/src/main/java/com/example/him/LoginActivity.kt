package com.example.him

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.him.databinding.ActivityLoginBinding
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

        val body = HashMap<String, String>()
        body.put("id", "example1")
        body.put("password", "123456")

        loginHandler(body)
    }

    private fun loginHandler(body: HashMap<String, String>) {
        RetrofitClient.instance.login(body).enqueue(object: Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                Log.d("Response", response.toString())
                Log.d("Response", "로그인 성공")
                Log.d("Response", response.body()?._id.toString())
                Log.d("Response", response.body()?.name.toString())
                Log.d("Response", response.body()?.id.toString())
                Log.d("Response", response.body()?.isProvider.toString())
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.d("Response", t.message.toString())
            }
        })
    }
}