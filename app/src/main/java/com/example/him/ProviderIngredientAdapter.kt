package com.example.him

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.him.databinding.ProviderIngredientRecyclerBinding
import java.text.SimpleDateFormat

class ProviderIngredientAdapter(var activity: RegisterOrderActivity) :
    RecyclerView.Adapter<ProviderIngredientHolder>() {
    var listIngredient = listOf<IngredientResponse>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProviderIngredientHolder {
        return ProviderIngredientHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.provider_ingredient_recycler, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return listIngredient.size
    }

    override fun onBindViewHolder(holder: ProviderIngredientHolder, position: Int) {
        holder.setIngredient(activity, listIngredient[position])
    }
}

class ProviderIngredientHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private lateinit var binding: ProviderIngredientRecyclerBinding

    @SuppressLint("SimpleDateFormat")
    fun setIngredient(activity: RegisterOrderActivity, ingredient: IngredientResponse) {
        binding = ProviderIngredientRecyclerBinding.bind(itemView)

        // binding.imageView. = ingredient.image
        binding.barcodeView.text = ingredient.barcode
        binding.nameView.text = ingredient.name
        binding.shelfLifeView.text =
            SimpleDateFormat("yyyy-MM-dd").format(ingredient.expirationDate)
        binding.priceView.text = "${ingredient.price}"

        binding.orderButton.setOnClickListener {
            val body = HashMap<String, String?>()
            body["orderIngredient"] = ingredient._id
            body["buyer"] = activity.intent.getStringExtra("userId")
            body["seller"] = ingredient.user
            Log.d("Ingredient", "$body")
            OrderManagementSystem().orderIngredient(activity, body)
        }
    }
}