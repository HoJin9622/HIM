package com.example.him

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.him.databinding.IngredientRecyclerBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat

class IngredientAdapter(var activity: AppCompatActivity) :
    RecyclerView.Adapter<IngredientHolder>() {
    var listIngredient = listOf<IngredientResponse>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientHolder {
        return IngredientHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.ingredient_recycler, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return listIngredient.size
    }

    override fun onBindViewHolder(holder: IngredientHolder, position: Int) {
        holder.setIngredient(activity, listIngredient[position])
    }
}

class IngredientHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private lateinit var binding: IngredientRecyclerBinding

    @SuppressLint("SimpleDateFormat")
    fun setIngredient(activity: AppCompatActivity, ingredient: IngredientResponse) {
        binding = IngredientRecyclerBinding.bind(itemView)

        // binding.imageView 사진 처리
        binding.barcodeView.text = ingredient.barcode
        binding.nameView.text = ingredient.name
        binding.shelfLifeView.text =
            SimpleDateFormat("yyyy-MM-dd").format(ingredient.expirationDate)
        binding.priceView.text = "${ingredient.price}"

        binding.editButton.setOnClickListener {
            val ims = IngredientManagementSystem()
            ims.edit(
                activity,
                ingredient._id,
                ingredient.name,
                ingredient.price.toString(),
                ingredient.barcode,
                ingredient.expirationDate
            )
        }
        binding.deleteButton.setOnClickListener {
            val ims = IngredientManagementSystem()
            ims.delete(activity, ingredient._id)
        }
    }
}