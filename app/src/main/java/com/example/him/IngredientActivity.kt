package com.example.him

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.him.databinding.ActivityIngredientBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.HashMap

class IngredientActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIngredientBinding

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIngredientBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        // View Binding 완료. 아래부터 작성.

        binding.confirmButton.setOnClickListener { registerIngredientHandler(intent.getStringExtra("userId")) }

        val dialog = DatePickerDialog(this)
        binding.shelfLifeEdit.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                dialog.show()
            }
        }
        dialog.setOnDateSetListener { _, year, month, dayOfMonth ->
            binding.shelfLifeEdit.text =
                Editable.Factory.getInstance().newEditable("${year}-${month + 1}-${dayOfMonth}")
        }
    }

    private fun registerIngredientHandler(userId: String?) {
        val body = HashMap<String, Any?>()
        body["user"] = userId
        body["name"] = binding.nameEdit.text.toString()
        body["expirationDate"] = binding.shelfLifeEdit.text.toString()
        body["price"] = binding.priceEdit.text.toString().toInt()
        body["image"] = ""
        body["barcode"] = ""

        RetrofitClient.instance.registerIngredient(body)
            .enqueue(object : Callback<IngredientResponse> {
                override fun onResponse(
                    call: Call<IngredientResponse>,
                    response: Response<IngredientResponse>
                ) {
                    Log.d("Response", response.toString())
                    if (response.code() == 201) {
                        Log.d("Response", "_id: ${response.body()?._id}")
                        Log.d("Response", "user: ${response.body()?.user}")
                        Log.d("Response", "name: ${response.body()?.name}")
                        Log.d("Response", "expirationDate: ${response.body()?.expirationDate}")
                        Log.d("Response", "image: ${response.body()?.image}")
                        Log.d("Response", "barcode: ${response.body()?.barcode}")
                        Toast.makeText(
                            this@IngredientActivity,
                            "식재료가 성공적으로 등록되었습니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                        moveMainPage(userId)
                    }
                }

                override fun onFailure(call: Call<IngredientResponse>, t: Throwable) {
                    Log.d("Response", t.message.toString())
                }
            })
    }

    fun moveMainPage(_id: String?) {
        startActivity(Intent(this, MainActivity::class.java).putExtra("userId", _id))
        finish()
    }
}