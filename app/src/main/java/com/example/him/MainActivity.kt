package com.example.him

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.him.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        // View Binding 완료. 아래부터 작성.

        binding.registerOrderButton.setOnClickListener { moveRegisterIngredientPage(intent.getStringExtra("userId")) }
        binding.navigateOrderButton.setOnClickListener { moveOrderListPage(intent.getStringExtra("userId")) }

        showIngredients(intent.getStringExtra("userId"))
    }

    private fun showIngredients(userId: String?) {
        RetrofitClient.instance.getIngredients(userId)
            .enqueue(object : Callback<ArrayList<IngredientResponse>> {
                override fun onResponse(
                    call: Call<ArrayList<IngredientResponse>>,
                    response: Response<ArrayList<IngredientResponse>>
                ) {
                    // val responseCode = response.code().toString()
                    Log.d("Response", "식재료 목록: " + response.body().toString())
                }

                override fun onFailure(call: Call<ArrayList<IngredientResponse>>, t: Throwable) {
                    Log.d("Response", t.message.toString())
                }
            })
    }

    private fun moveRegisterIngredientPage(_id: String?) {
        startActivity(Intent(this, RegisterIngredientActivity::class.java).putExtra("userId", _id))
    }

    private fun moveOrderListPage(_id: String?) {
        startActivity(Intent(this, OrderListActivity::class.java).putExtra("userId", _id))
    }
}