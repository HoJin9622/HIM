package com.example.him

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.ContentResolver
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.example.him.databinding.ActivityIngredientBinding
import com.google.firebase.storage.FirebaseStorage
import com.google.zxing.integration.android.IntentIntegrator
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okio.BufferedSink
import java.io.File
import java.io.FileInputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


class IngredientActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIngredientBinding

    private var PICK_IMAGE_FROM_ALBUM = 0
    private var storage: FirebaseStorage? = null
    private var photoUri : Uri? = null
    private var cloudUri : String? = null

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIngredientBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        // View Binding 완료. 아래부터 작성.
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        storage = FirebaseStorage.getInstance()

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
                Glide.with(binding.photoButton.context).load(ingredient.image).into(binding.photoButton)
            }
            else -> {
                Log.d("Response", "userId: null, ingredient: null")
                Toast.makeText(this, "로그인 정보를 확인할 수 없습니다.", Toast.LENGTH_SHORT).show()
                moveLoginPage()
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

    private fun moveLoginPage() {
        startActivity(Intent(this, LoginActivity::class.java))
        finishAffinity()
    }

    private fun registerIngredientHandler(userId: String) {
        if (!inputCheck()) return
        val body = HashMap<String, Any?>()
        body["user"] = userId
        body["image"] = cloudUri
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
        body["image"] = cloudUri
        body["price"] = binding.priceEdit.text.toString().toInt()
        IngredientManagementSystem().edit(this, body)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            val barcode = result.contents
            if (barcode == null) {
                Toast.makeText(this, "오류가 발생했습니다.\n다시 시도해주세요.", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "스캔 완료.\n$barcode", Toast.LENGTH_LONG).show()
                binding.barcodeEdit.setText(barcode)
                IngredientManagementSystem().searchByBarcode(this, binding, barcode)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
            if(requestCode == PICK_IMAGE_FROM_ALBUM) {
                if(resultCode == Activity.RESULT_OK) {
                    photoUri = data?.data
                    binding.photoButton.setImageURI(photoUri)
                    contentUpload()
                } else {

                }
            }
        }
    }

    private fun contentUpload() {
        var timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        var imageFileName = "IMAGE_" + timestamp + "_.png"

        var storageRef = storage?.reference?.child("images")?.child(imageFileName)

        storageRef?.putFile(photoUri!!)?.addOnSuccessListener {
            Toast.makeText(this, "업로드 성공", Toast.LENGTH_LONG).show()
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                cloudUri = uri.toString()
            }
        }
    }
}