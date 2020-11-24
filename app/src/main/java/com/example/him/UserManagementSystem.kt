package com.example.him

import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserManagementSystem {
    fun login(activity: AppCompatActivity, id: String, password: String): String? {
        var responseId: String? = null
        val body = HashMap<String, String>()
        body["id"] = id
        body["password"] = password
        RetrofitClient.instance.login(body).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.code() == 200) {
                    Log.d("Response", response.toString())
                    Log.d("Response", "_id: ${response.body()?._id}")
                    Log.d("Response", "name: ${response.body()?.name}")
                    Log.d("Response", "id: ${response.body()?.id}")
                    Log.d("Response", "isProvider: ${response.body()?.isProvider}")

                    Toast.makeText(activity, "로그인 성공", Toast.LENGTH_SHORT).show()
                    responseId = response.body()?._id
                } else {
                    Toast.makeText(
                        activity,
                        "아이디 혹은 비밀번호가 올바르지 않습니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.d("Response", t.message.toString())
            }
        })
        return responseId
    }
}