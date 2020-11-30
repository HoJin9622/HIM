package com.example.him

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.him.databinding.ActivityIngredientBinding
import com.google.zxing.integration.android.IntentIntegrator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat


class IngredientActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIngredientBinding

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIngredientBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        // View Binding 완료. 아래부터 작성.

        val userId = intent.getStringExtra("userId")
        val ingredient = intent.getSerializableExtra("ingredient") as IngredientResponse?
        when {
            userId != null -> {
                binding.confirmButton.setOnClickListener { registerIngredientHandler(userId) }
            }
            ingredient != null -> {
                binding.nameEdit.setText(ingredient.name)
                binding.priceEdit.setText(ingredient.price.toString())
                binding.barcodeEdit.setText(ingredient.barcode)
                binding.shelfLifeEdit.setText(SimpleDateFormat("yyyy-MM-dd").format(ingredient.expirationDate))
                binding.confirmButton.setOnClickListener { editIngredientHandler(ingredient._id) }
            }
            else -> {
                Log.d("Response", "userId: null, ingredient: null")
                Toast.makeText(this, "로그인 정보를 확인할 수 없습니다.", Toast.LENGTH_SHORT).show()
                moveLoginPage()
                finish()
            }
        }

        val dialog = DatePickerDialog(this)
        binding.shelfLifeEdit.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                dialog.show()
            }
        }
        dialog.setOnDateSetListener { _, year, month, dayOfMonth ->
            val date = "${year}-${month + 1}-${dayOfMonth}"
            binding.shelfLifeEdit.setText(date)
        }
        binding.barcodeButton.setOnClickListener {
            IntentIntegrator(this).initiateScan()
        }
    }

    private fun inputCheck(): Boolean {
        when {
            binding.nameEdit.text.isEmpty() -> {
                Toast.makeText(this, "이름을 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
            binding.shelfLifeEdit.text.isEmpty() -> {
                Toast.makeText(this, "유통기한을 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
            binding.priceEdit.text.isEmpty() -> {
                Toast.makeText(this, "가격을 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
            else -> {
                return true
            }
        }
        return false
    }

    private fun registerIngredientHandler(userId: String) {
        if (!inputCheck()) return
        val body = HashMap<String, Any?>()
        body["user"] = userId
        body["image"] = ""
        body["barcode"] = binding.barcodeEdit.text.toString()
        body["name"] = binding.nameEdit.text.toString()
        body["expirationDate"] = binding.shelfLifeEdit.text.toString()
        body["price"] = binding.priceEdit.text.toString().toInt()
        IngredientManagementSystem().register(this, body)
    }

    private fun editIngredientHandler(ingredientId: String) {
        if (!inputCheck()) return
        val body = HashMap<String, Any?>()
        body["_id"] = ingredientId
        body["barcode"] = binding.barcodeEdit.text.toString()
        body["name"] = binding.nameEdit.text.toString()
        body["expirationDate"] = binding.shelfLifeEdit.text.toString()
        body["image"] = ""
        body["price"] = binding.priceEdit.text.toString().toInt()
        IngredientManagementSystem().edit(this, body)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "오류 - 다시 시도해주세요.", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "스캔완료: " + result.contents, Toast.LENGTH_LONG).show()
                binding.barcodeEdit.setText(result.contents)

                RetrofitClient.instance.getIngredient(result.contents)
                    .enqueue(object : Callback<IngredientResponse> {
                        override fun onResponse(
                            call: Call<IngredientResponse>,
                            response: Response<IngredientResponse>
                        ) {
                            Log.d("Response", "결과: $response")
                            binding.nameEdit.setText(response.body()?.name)
                            binding.priceEdit.setText(response.body()?.price.toString())
                        }

                        override fun onFailure(call: Call<IngredientResponse>, t: Throwable) {
                            Log.d("Response", t.message.toString())
                            Toast.makeText(
                                this@IngredientActivity, "서버와의 접속이 원활하지 않습니다.", Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun moveLoginPage() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}