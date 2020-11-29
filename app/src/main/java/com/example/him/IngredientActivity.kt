package com.example.him

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.him.databinding.ActivityIngredientBinding
import com.google.zxing.integration.android.IntentIntegrator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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

        binding.barcodeButton.setOnClickListener {
            IntentIntegrator(this).initiateScan()
        }
    }

    private fun registerIngredientHandler(userId: String?) {
        val ims = IngredientManagementSystem()
        if (userId != null) {
            val body = HashMap<String, Any?>()
            body["user"] = userId
            body["image"] = ""
            body["barcode"] = binding.barcodeEdit.text.toString()
            body["name"] = binding.nameEdit.text.toString()
            body["expirationDate"] = binding.shelfLifeEdit.text.toString()
            body["price"] = binding.priceEdit.text.toString().toInt()
            ims.register(this, body)
        }
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
                            Log.d("Response", "결과: ${response.toString()}")
                            binding.nameEdit.setText(response.body()?.name)
                            binding.priceEdit.setText(response.body()?.price.toString())
                        }

                        override fun onFailure(call: Call<IngredientResponse>, t: Throwable) {
                            Log.d("Response", t.message.toString())
                        }
                    })
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}