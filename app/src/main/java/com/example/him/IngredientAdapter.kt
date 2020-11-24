package com.example.him

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class IngredientAdapter : RecyclerView.Adapter<Holder>() {
    var listIngredient = mutableListOf<IngredientResponse>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            LayoutInflater.from(parent.context).inflate(R.layout.activity_main, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return listIngredient.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val ingredient = listIngredient[position]
        holder.setIngredient(ingredient)
        var name: String
        var expirationDate: String
        var barcode: String
        var price: Int
        var image: String
        // val
    }
}

class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun setIngredient(ingredient: IngredientResponse) {

    }
}