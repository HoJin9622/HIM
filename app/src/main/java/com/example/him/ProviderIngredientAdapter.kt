package com.example.him

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.him.databinding.ProviderIngredientRecyclerBinding
import java.text.SimpleDateFormat

class ProviderIngredientAdapter(var activity: AppCompatActivity) :
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

class ProviderIngredientHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private lateinit var binding: ProviderIngredientRecyclerBinding

    @SuppressLint("SimpleDateFormat")
    fun setIngredient(activity: AppCompatActivity, ingredient: IngredientResponse) {
        binding = ProviderIngredientRecyclerBinding.bind(itemView)

        // binding.imageView. = ingredient.image
        binding.barcodeView.text = ingredient.barcode
        binding.nameView.text = ingredient.name
        binding.shelfLifeView.text =
            SimpleDateFormat("yyyy-MM-dd").format(ingredient.expirationDate)
        binding.priceView.text = "${ingredient.price}"

        binding.orderButton.setOnClickListener { }
    }
}