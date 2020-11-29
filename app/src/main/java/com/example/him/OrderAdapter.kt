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
import com.example.him.databinding.OrderRecyclerBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

    @SuppressLint("SimpleDateFormat")
    fun setOrder(activity: AppCompatActivity, order: OrderResponse) {
        binding = OrderRecyclerBinding.bind(itemView)

        // binding.imageView 사진 처리
        binding.ingredientView.text = order.orderIngredient.name
        binding.consumerView.text = order.buyer.name
        binding.providerView.text = order.seller.name

        binding.deleteButton.setOnClickListener {
            RetrofitClient.instance.deleteIngredients(order._id)
                .enqueue(object : Callback<MessageResponse> {
                    override fun onResponse(
                        call: Call<MessageResponse>,
                        response: Response<MessageResponse>
                    ) {
                        Log.d("Response", "결과: $response")

                        val userId = activity.intent.getStringExtra("userId")
                        activity.finish()
                        activity.startActivity(
                            Intent(
                                activity,
                                MainActivity::class.java
                            ).putExtra("userId", userId)
                        )
                        Toast.makeText(activity, "해당 주문이 취소되었습니다.", Toast.LENGTH_SHORT).show()
                    }

                    override fun onFailure(call: Call<MessageResponse>, t: Throwable) {
                        Log.d("Response", t.message.toString())
                        Toast.makeText(activity, "서버와의 접속이 원활하지 않습니다.", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }
}