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

        val userId = intent.getStringExtra("userId")
        if (userId == null) {
            Log.d("Response", "userId: null")
            Toast.makeText(this, "로그인 정보를 확인할 수 없습니다.", Toast.LENGTH_SHORT).show()
            moveLoginPage()
            return
        }

        showIngredients(userId)
        binding.navigateEditUserButton.setOnClickListener { moveEditUserPage(userId) }
        binding.registerOrderButton.setOnClickListener { moveRegisterIngredientPage(userId) }
        binding.navigateOrderButton.setOnClickListener { moveOrderListPage(userId) }
    }

    private fun showIngredients(userId: String) {
        RetrofitClient.instance.getIngredients(userId)
            .enqueue(object : Callback<ArrayList<IngredientResponse>> {
                override fun onResponse(
                    call: Call<ArrayList<IngredientResponse>>,
                    response: Response<ArrayList<IngredientResponse>>
                ) {
                    Log.d("Response", "식재료 목록: ${response.body()}")
                    val ingredientList: ArrayList<IngredientResponse>? = response.body()
                    val adapter = IngredientAdapter(this@MainActivity)
                    if (ingredientList != null) {
                        adapter.listIngredient = ArrayList<IngredientResponse>(ingredientList)
                        binding.recyclerView.adapter = adapter
                        binding.recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
                    } else {
                        Log.d("Response", "ingredientList: null")
                        Toast.makeText(this@MainActivity, "서버와의 접속이 원활하지 않습니다.", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onFailure(call: Call<ArrayList<IngredientResponse>>, t: Throwable) {
                    Log.d("Response", t.message.toString())
                    Toast.makeText(this@MainActivity, "서버와의 접속이 원활하지 않습니다.", Toast.LENGTH_SHORT)
                        .show()
                }
            })
    }

    private fun moveLoginPage() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun moveEditUserPage(userId: String) {
        startActivity(Intent(this, EditUserActivity::class.java).putExtra("userId", userId))
    }

    private fun moveRegisterIngredientPage(userId: String) {
        startActivity(Intent(this, IngredientActivity::class.java).putExtra("userId", userId))
    }

    private fun moveOrderListPage(userId: String) {
        startActivity(Intent(this, OrderListActivity::class.java).putExtra("userId", userId))
    }
}