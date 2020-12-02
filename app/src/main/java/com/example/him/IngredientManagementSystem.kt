package com.example.him

import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.him.databinding.ActivityIngredientBinding
import com.example.him.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.HashMap

// 식재료 관리 시스템 클래스. 식재료 정보에 접근하는 기능들을 갖는다.
class IngredientManagementSystem {
    // 식재료 목록을 보여주는 함수.
    fun show(activity: MainActivity, binding: ActivityMainBinding, userId: String?) {
        RetrofitClient.instance.getIngredients(userId)
            .enqueue(object : Callback<ArrayList<IngredientResponse>> {
                override fun onResponse(
                    call: Call<ArrayList<IngredientResponse>>,
                    response: Response<ArrayList<IngredientResponse>>
                ) {
                    Log.d("Response", "식재료 목록: ${response.body()}")
                    val ingredientList: ArrayList<IngredientResponse>? = response.body()
                    val adapter = IngredientAdapter(activity)
                    if (ingredientList != null) {
                        adapter.listIngredient = ArrayList(ingredientList)
                        binding.recyclerView.adapter = adapter
                        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
                    } else {
                        Log.d("Response", "ingredientList: null")
                    }
                }


                override fun onFailure(call: Call<ArrayList<IngredientResponse>>, t: Throwable) {
                    Log.d("Response", t.message.toString())
                    Toast.makeText(activity, "서버와의 접속이 원활하지 않습니다.", Toast.LENGTH_SHORT).show()
                }
            })
    }

    // 식재료를 등록하는 함수.
    fun register(activity: IngredientActivity, body: HashMap<String, Any?>) {
        RetrofitClient.instance.registerIngredient(body)
            .enqueue(object : Callback<IngredientResponse> {
                override fun onResponse(
                    call: Call<IngredientResponse>, response: Response<IngredientResponse>
                ) {
                    Log.d("Response", response.toString())
                    if (response.code() == 201) {
                        Log.d("Response", "_id: ${response.body()?._id}")
                        Log.d("Response", "user: ${response.body()?.user}")
                        Log.d("Response", "name: ${response.body()?.name}")
                        Log.d("Response", "expirationDate: ${response.body()?.expirationDate}")
                        Log.d("Response", "image: ${response.body()?.image}")
                        Log.d("Response", "barcode: ${response.body()?.barcode}")
                        activity.finish()
                    } else {
                        Log.d("Response", "response.code(): ${response.code()}")
                        Toast.makeText(activity, "통신 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<IngredientResponse>, t: Throwable) {
                    Log.d("Response", t.message.toString())
                    Toast.makeText(activity, "서버와의 접속이 원활하지 않습니다.", Toast.LENGTH_SHORT).show()
                }
            })
    }

    // 식재료를 수정하는 함수.
    fun edit(activity: IngredientActivity, body: HashMap<String, Any?>) {
        RetrofitClient.instance.editIngredient(body).enqueue(object : Callback<IngredientResponse> {
            override fun onResponse(
                call: Call<IngredientResponse>, response: Response<IngredientResponse>
            ) {
                Log.d("Response", response.toString())
                if (response.code() == 200) {
                    Log.d("Response", "_id: ${response.body()?._id}")
                    Log.d("Response", "user: ${response.body()?.user}")
                    Log.d("Response", "name: ${response.body()?.name}")
                    Log.d("Response", "expirationDate: ${response.body()?.expirationDate}")
                    Log.d("Response", "image: ${response.body()?.image}")
                    Log.d("Response", "barcode: ${response.body()?.barcode}")
                    activity.finish()
                } else {
                    Log.d("Response", "response.code(): ${response.code()}")
                    Toast.makeText(activity, "통신 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<IngredientResponse>, t: Throwable) {
                Log.d("Response", t.message.toString())
                Toast.makeText(activity, "서버와의 접속이 원활하지 않습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // 식재료를 삭제하는 함수.
    fun delete(activity: MainActivity, ingredientId: String) {
        RetrofitClient.instance.deleteIngredients(ingredientId)
            .enqueue(object : Callback<MessageResponse> {
                override fun onResponse(
                    call: Call<MessageResponse>,
                    response: Response<MessageResponse>
                ) {
                    Log.d("Response", "결과: $response")
                    if (response.code() == 400) {
                        Toast.makeText(
                            activity,
                            "주문 신청된 식재료입니다.\n먼저 주문 취소를 해주세요.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        activity.showIngredients()
                        Toast.makeText(activity, "해당 식재료가 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<MessageResponse>, t: Throwable) {
                    Log.d("Response", t.message.toString())
                    Toast.makeText(activity, "서버와의 접속이 원활하지 않습니다.", Toast.LENGTH_SHORT).show()
                }
            })
    }

    // 바코드에 해당하는 식재료를 검색하는 함수.
    fun searchByBarcode(
        activity: IngredientActivity,
        binding: ActivityIngredientBinding,
        body: HashMap<String, String?>
    ) {

        RetrofitClient.instance.getIngredient(body)
            .enqueue(object : Callback<IngredientResponse> {
                override fun onResponse(
                    call: Call<IngredientResponse>, response: Response<IngredientResponse>
                ) {
                    Log.d("Response", "결과: ${response.body()}")
                    if (response.body() != null) {
                        binding.nameEdit.setText(response.body()!!.name)
                        binding.priceEdit.setText(response.body()!!.price.toString())
                        Glide.with(binding.photoButton.context).load(response.body()?.image)
                            .into(binding.photoButton)
                    }
                }

                override fun onFailure(call: Call<IngredientResponse>, t: Throwable) {
                    Log.d("Response", t.message.toString())
                    Toast.makeText(activity, "서버와의 접속이 원활하지 않습니다.", Toast.LENGTH_SHORT).show()
                }
            })
    }
}