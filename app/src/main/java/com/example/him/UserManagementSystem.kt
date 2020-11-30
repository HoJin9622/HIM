package com.example.him

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserManagementSystem {
    fun login(activity: LoginActivity, id: String, password: String) {
        val body = HashMap<String, String>()
        body["userId"] = id
        body["password"] = password
        RetrofitClient.instance.login(body).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.code() == 200) {
                    val user = response.body()
                    Log.d("Response", "user: $user")
                    if (user != null) {
                        Log.d("Response", "_id: ${user._id}")
                        Log.d("Response", "id: ${user.userId}")
                        Log.d("Response", "isProvider: ${user.isProvider}")
                        Log.d("Response", "name: ${user.name}")
                        activity.startActivity(
                            Intent(activity, MainActivity::class.java).putExtra("userId", user._id)
                        )
                        activity.finish()
                    } else {
                        Toast.makeText(activity, "서버와의 접속이 원활하지 않습니다.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.d("Response", "response.code(): ${response.code()}")
                    Toast.makeText(activity, "입력된 정보가 존재하지 않습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.d("Response", t.message.toString())
                Toast.makeText(activity, "서버와의 접속이 원활하지 않습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun register(
        activity: RegisterUserActivity,
        id: String,
        password: String,
        name: String,
        isProvider: Boolean
    ) {
        val body = HashMap<String, Any>()
        body["userId"] = id
        body["password"] = password
        body["name"] = name
        body["isProvider"] = isProvider
        RetrofitClient.instance.register(body).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                Log.d("Response", response.toString())
                if (response.code() == 201) {
                    Log.d("Response", "_id: ${response.body()?._id}")
                    Log.d("Response", "name: ${response.body()?.name}")
                    Log.d("Response", "userId: ${response.body()?.userId}")
                    Log.d("Response", "isProvider: ${response.body()?.isProvider}")

                    Toast.makeText(activity, "가입해주셔서 감사합니다.", Toast.LENGTH_SHORT).show()
                    activity.startActivity(
                        Intent(activity, MainActivity::class.java).putExtra(
                            "userId",
                            response.body()?._id
                        )
                    )
                    activity.finishAffinity()
                } else {
                    Toast.makeText(
                        activity, "이미 존재하는 아이디입니다.", Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.d("Response", t.message.toString())
                Toast.makeText(activity, "서버와의 접속이 원활하지 않습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun withdraw(activity: EditUserActivity, userId: String) {
        RetrofitClient.instance.deleteUser(userId).enqueue(object : Callback<MessageResponse> {
            override fun onResponse(
                call: Call<MessageResponse>,
                response: Response<MessageResponse>
            ) {
                Log.d("Response", "결과: $response")
                Toast.makeText(activity, "회원이 탈퇴되었습니다.", Toast.LENGTH_SHORT).show()
                activity.startActivity(Intent(activity, LoginActivity::class.java))
                activity.finishAffinity()
            }

            override fun onFailure(call: Call<MessageResponse>, t: Throwable) {
                Log.d("Response", t.message.toString())
                Toast.makeText(activity, "서버와의 접속이 원활하지 않습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun edit(activity: EditUserActivity, id: String, password: String) {
        val body = HashMap<String, String?>()
        body["_id"] = id
        body["password"] = password
        RetrofitClient.instance.editUser(body).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                Log.d("Response", response.toString())
                if (response.code() == 200) {
                    Log.d("Response", "_id: ${response.body()?._id}")
                    Log.d("Response", "name: ${response.body()?.name}")
                    Log.d("Response", "userId: ${response.body()?.userId}")
                    Log.d("Response", "isProvider: ${response.body()?.isProvider}")
                    Toast.makeText(activity, "비밀번호가 변경되었습니다.", Toast.LENGTH_SHORT).show()
                    activity.finish()
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.d("Response", t.message.toString())
                Toast.makeText(activity, "서버와의 접속이 원활하지 않습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}