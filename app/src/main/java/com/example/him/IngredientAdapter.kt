package com.example.him

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class IngredientAdapter : RecyclerView.Adapter<Holder>() {
    var listIngredient = ArrayList<IngredientResponse>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            LayoutInflater.from(parent.context).inflate(R.layout.activity_main, parent, false)
        )
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        return listIngredient.size
    }

}

class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

}