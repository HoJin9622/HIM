package com.example.him

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

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
        val name: String
        val price: Int
        // val
    }
}

class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

}