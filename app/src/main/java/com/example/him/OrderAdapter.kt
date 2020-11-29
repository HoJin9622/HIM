package com.example.him

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.him.databinding.OrderRecyclerBinding

class OrderAdapter(var activity: AppCompatActivity) : RecyclerView.Adapter<OrderHolder>() {
    var listOrder = listOf<OrderResponse>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHolder {
        return OrderHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.order_recycler, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return listOrder.size
    }

    override fun onBindViewHolder(holder: OrderHolder, position: Int) {
        holder.setOrder(activity, listOrder[position])
    }
}

class OrderHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private lateinit var binding: OrderRecyclerBinding

    fun setOrder(activity: AppCompatActivity, order: OrderResponse) {
        binding = OrderRecyclerBinding.bind(itemView)

        binding.ingredientView.text = order.orderIngredient.name
        binding.consumerView.text = order.buyer.name
        binding.providerView.text = order.seller.name

        binding.deleteButton.setOnClickListener {
            OrderManagementSystem().delete(activity, order._id)
        }
    }
}