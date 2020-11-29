package com.example.him

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.him.databinding.ActivityMainBinding
import com.example.him.databinding.ActivityRegisterOrderBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class RegisterOrderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterOrderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterOrderBinding.inflate(layoutInflater)
        var view = binding.root
        setContentView(R.layout.activity_register_order)
        // View Binding 완료. 아래부터 작성.

        fetchProvider()
    }

    private fun fetchProvider() {
        RetrofitClient.instance.getProvider()
            .enqueue(object : Callback<ArrayList<UserResponse>> {
                override fun onResponse(
                    call: Call<ArrayList<UserResponse>>,
                    response: Response<ArrayList<UserResponse>>
                ) {
                    // val responseCode = response.code().toString()
                    Log.d("Response", "공급자 목록: " + response.body().toString())
                }

                override fun onFailure(call: Call<ArrayList<UserResponse>>, t: Throwable) {
                    Log.d("Response", t.message.toString())
                    Toast.makeText(
                        this@RegisterOrderActivity,
                        "서버와의 접속이 원활하지 않습니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}