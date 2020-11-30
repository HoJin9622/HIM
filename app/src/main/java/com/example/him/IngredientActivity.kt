package com.example.him

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.FileUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.him.databinding.ActivityIngredientBinding
import com.google.zxing.integration.android.IntentIntegrator
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


class IngredientActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIngredientBinding

    var PICK_IMAGE_FROM_ALBUM = 0
    var photoUri: Uri? = null

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIngredientBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        // View Binding 완료. 아래부터 작성.

        var photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        binding.photoButton.setOnClickListener {
            startActivityForResult(photoPickerIntent, PICK_IMAGE_FROM_ALBUM)
        }


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
            if (requestCode == PICK_IMAGE_FROM_ALBUM) {
                if (resultCode == Activity.RESULT_OK) {
                    photoUri = data?.data
                    Log.d("Response", photoUri.toString())
                    contentUpload(photoUri.toString())
                } else {
                    finish()
                }
            }
        }
    }

    private fun contentUpload(path: String) {
        val file = File(path)

        var fileName = "image.png"

        var requestBody : RequestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        var body : MultipartBody.Part = MultipartBody.Part.createFormData("image", fileName, requestBody)

        RetrofitClient.instance.uploadImage(body)
            .enqueue(object : Callback<String> {
                override fun onResponse(
                    call: Call<String>,
                    response: Response<String>
                ) {
                    Log.d("Response", "결과: $response")
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.d("Response", "오류: ${t.message.toString()}")
                    Toast.makeText(
                        this@IngredientActivity, "서버와의 접속이 원활하지 않습니다.", Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }


    private fun moveLoginPage() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}