package com.example.him

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

        showIngredients()
    }

    private fun showIngredients() {
        RetrofitClient.instance.getIngredients().enqueue(object: Callback<ArrayList<IngredientResponse>>{
            override fun onFailure(call: Call<ArrayList<IngredientResponse>>, t: Throwable) {
                Log.d("Response", t.message.toString())
            }

            override fun onResponse(
                call: Call<ArrayList<IngredientResponse>>,
                response: Response<ArrayList<IngredientResponse>>
            ) {
                val responseCode = response.code().toString()
                Log.d("Response", responseCode)
                Log.d("Response", response.body().toString())
            }
        })
    }
}