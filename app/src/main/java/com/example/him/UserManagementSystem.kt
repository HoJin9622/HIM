package com.example.him

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// 사용자 관리 시스템 클래스. 사용자 회원 정보에 접근하는 기능들을 갖는다.
class UserManagementSystem {
    // 로그인 함수.
    fun login(activity: LoginActivity, id: String, password: String) {
        val body = HashMap<String, String>()
        body["userId"] = id
        body["password"] = password
        // HTTP 통신 중 POST 명령을 수행하여 로그인 정보를 검증한다.
        RetrofitClient.instance.login(body).enqueue(object : Callback<UserResponse> {
            // HTTP POST에 대한 정상적인 응답을 수실할 때 수행하는 함수.
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.code() == 200) {
                    val user = response.body()
                    Log.d("Response", "user: $user")
                    if (user != null) {
                        Log.d("Response", "_id: ${user._id}")
                        Log.d("Response", "id: ${user.userId}")
                        Log.d("Response", "isProvider: ${user.isProvider}")
                        Log.d("Response", "name: ${user.name}")
                        //로그인한 아이디를 받아 메인 화면을 출력한다.
                        activity.startActivity(
                            Intent(activity, MainActivity::class.java).putExtra("userId", user._id)
                        )
                        //로그인 함수를 닫는다.
                        activity.finish()
                    } else {
                        Toast.makeText(activity, "서버와의 접속이 원활하지 않습니다.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.d("Response", "response.code(): ${response.code()}")
                    Toast.makeText(activity, "입력된 정보가 존재하지 않습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            // HTTP POST에 대한 응답이 실패할 때 수행하는 함수.
            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.d("Response", t.message.toString())
                Toast.makeText(activity, "서버와의 접속이 원활하지 않습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // 회원가입 함수.
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
        // HTTP 통신 중 POST 명령을 수행하여 회원가입을 할 때 정보를 검증한다.
        RetrofitClient.instance.register(body).enqueue(object : Callback<UserResponse> {
            // HTTP POST에 대한 정상적인 응답을 수실할 때 수행하는 함수.
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                Log.d("Response", response.toString())
                if (response.code() == 201) {
                    Log.d("Response", "_id: ${response.body()?._id}")
                    Log.d("Response", "name: ${response.body()?.name}")
                    Log.d("Response", "userId: ${response.body()?.userId}")
                    Log.d("Response", "isProvider: ${response.body()?.isProvider}")

                    Toast.makeText(activity, "가입해주셔서 감사합니다.", Toast.LENGTH_SHORT).show()
                    //회원가입한 아이디를 받아서 메인화면을 출력한다.
                    activity.startActivity(
                        Intent(activity, MainActivity::class.java).putExtra(
                            "userId",
                            response.body()?._id
                        )
                    )
                    //모든 화면을 종료하고 메인 화면으로 넘어간다.
                    activity.finishAffinity()
                } else {
                    Toast.makeText(
                        activity, "이미 존재하는 아이디입니다.", Toast.LENGTH_SHORT
                    ).show()
                }
            }

            // HTTP POST에 대한 응답이 실패할 때 수행하는 함수.
            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.d("Response", t.message.toString())
                Toast.makeText(activity, "서버와의 접속이 원활하지 않습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // 회원 탈퇴 함수.
    fun withdraw(activity: EditUserActivity, userId: String) {
        // HTTP 통신 중 DELETE 명령을 수행하여 로그인된 아이디를 삭제한다.
        RetrofitClient.instance.deleteUser(userId).enqueue(object : Callback<MessageResponse> {
            // HTTP DELETE에 대한 정상적인 응답을 수실할 때 수행하는 함수.
            override fun onResponse(
                call: Call<MessageResponse>,
                response: Response<MessageResponse>
            ) {
                Log.d("Response", "결과: $response")
                Toast.makeText(activity, "회원이 탈퇴되었습니다.", Toast.LENGTH_SHORT).show()
                activity.startActivity(Intent(activity, LoginActivity::class.java))
                //모든 화면을 종료하고 로그인 화면으로 넘어간다.
                activity.finishAffinity()
            }

            // HTTP DELETE에 대한 응답이 실패할 때 수행하는 함수.
            override fun onFailure(call: Call<MessageResponse>, t: Throwable) {
                Log.d("Response", t.message.toString())
                Toast.makeText(activity, "서버와의 접속이 원활하지 않습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // 회원 변경 함수.
    fun edit(activity: EditUserActivity, id: String, password: String) {
        val body = HashMap<String, String?>()
        body["_id"] = id
        body["password"] = password
        // HTTP 통신 중 PUT 명령을 수행하여 로그인된 회원의 정보를 변경한다.
        RetrofitClient.instance.editUser(body).enqueue(object : Callback<UserResponse> {
            // HTTP PUT에 대한 정상적인 응답을 수실할 때 수행하는 함수.
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                Log.d("Response", response.toString())
                if (response.code() == 200) {
                    Log.d("Response", "_id: ${response.body()?._id}")
                    Log.d("Response", "name: ${response.body()?.name}")
                    Log.d("Response", "userId: ${response.body()?.userId}")
                    Log.d("Response", "isProvider: ${response.body()?.isProvider}")
                    Toast.makeText(activity, "비밀번호가 변경되었습니다.", Toast.LENGTH_SHORT).show()
                    //회원 변경 함수를 종료한다.
                    activity.finish()
                }
            }

            // HTTP PUT에 대한 응답이 실패할 때 수행하는 함수.
            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.d("Response", t.message.toString())
                Toast.makeText(activity, "서버와의 접속이 원활하지 않습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}