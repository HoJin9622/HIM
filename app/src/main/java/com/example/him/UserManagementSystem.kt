package com.example.him

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserManagementSystem {
    fun login(activity: AppCompatActivity, id: String, password: String) {
        val body = HashMap<String, String>()
        body["userId"] = id
        body["password"] = password
        RetrofitClient.instance.login(body).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.code() == 200) {
                    Log.d("Response", response.toString())
                    Log.d("Response", "_id: ${response.body()?._id}")
                    Log.d("Response", "name: ${response.body()?.name}")
                    Log.d("Response", "id: ${response.body()?.userId}")
                    Log.d("Response", "isProvider: ${response.body()?.isProvider}")

                    Toast.makeText(activity, "로그인 성공", Toast.LENGTH_SHORT).show()
                    activity.startActivity(
                        Intent(activity, MainActivity::class.java).putExtra(
                            "userId",
                            response.body()?._id
                        )
                    )
                    activity.finish()
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
    }

    fun register(activity: AppCompatActivity, id: String, password: String, name: String, isProvider: Boolean) {
        val body = HashMap<String, Any>()
        body["userId"] = id
        body["password"] = password
        body["name"] = name
        body["isProvider"] = isProvider

        RetrofitClient.instance.register(body).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                Log.d("Response", response.toString())
                if (response.code() == 201) {
                    Log.d("Response", "_id: " + response.body()?._id.toString())
                    Log.d("Response", "name: " + response.body()?.name.toString())
                    Log.d("Response", "userId: " + response.body()?.userId.toString())
                    Log.d("Response", "isProvider: " + response.body()?.isProvider.toString())

                    Toast.makeText(activity, "회원가입 성공", Toast.LENGTH_SHORT).show()
                    activity.startActivity(
                        Intent(activity, MainActivity::class.java).putExtra(
                            "userId",
                            response.body()?._id
                        )
                    )
                    activity.finish()
                } else {
                    Toast.makeText(
                        activity, "이미 존재하는 아이디입니다.", Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.d("Response", t.message.toString())
            }
        })
    }

}