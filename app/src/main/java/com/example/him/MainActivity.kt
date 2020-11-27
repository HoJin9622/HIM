package com.example.him

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
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

        binding.registerOrderButton.setOnClickListener {
            moveRegisterIngredientPage(
                intent.getStringExtra(
                    "userId"
                )
            )
        }
        binding.navigateOrderButton.setOnClickListener { moveOrderListPage(intent.getStringExtra("userId")) }
        binding.navigateEditUserButton.setOnClickListener { moveEditUserPage(intent.getStringExtra("userId")) }
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

                    val ingredientList: ArrayList<IngredientResponse>? = response.body()
                    val adapter = IngredientAdapter(this@MainActivity)
                    if (ingredientList != null) {
                        adapter.listIngredient = ingredientList
                        binding.recyclerView.adapter = adapter
                        binding.recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
                    } else {
                        Log.d("Response", "ingredientList: null")
                    }
                }

                override fun onFailure(call: Call<ArrayList<IngredientResponse>>, t: Throwable) {
                    Log.d("Response", t.message.toString())
                    Toast.makeText(this@MainActivity, "서버와의 접속이 원활하지 않습니다.", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun moveRegisterIngredientPage(_id: String?) {
        startActivity(Intent(this, IngredientActivity::class.java).putExtra("userId", _id))
    }

    private fun moveOrderListPage(_id: String?) {
        startActivity(Intent(this, OrderListActivity::class.java).putExtra("userId", _id))
    }

    private fun moveEditUserPage(_id: String?) {
        startActivity(Intent(this, EditUserActivity::class.java).putExtra("userId", _id))
    }
}