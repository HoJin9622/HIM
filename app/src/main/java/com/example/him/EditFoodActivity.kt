package com.example.him

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.him.databinding.ActivityEditFoodBinding

class EditFoodActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditFoodBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditFoodBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        // View Binding 완료. 아래부터 작성.

    }
}