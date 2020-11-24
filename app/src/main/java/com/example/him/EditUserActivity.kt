package com.example.him

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.him.databinding.ActivityEditUserBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditUserBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        // View Binding 완료. 아래부터 작성.

        binding.withdrawButton.setOnClickListener { withdrawHandler(intent.getStringExtra("userId")) }
        binding.modifyButton.setOnClickListener { userEditHandler() }
    }

    private fun withdrawHandler(userId: String?) {
        val ums = UserManagementSystem()

        if (userId != null) {
            ums.withdraw(this, userId)
        }
    }

    private fun userEditHandler() {
        val body = HashMap<String, String?>()
        body["_id"] = intent.getStringExtra("userId")
        body["password"] = binding.passwordEdit.text.toString()

        RetrofitClient.instance.editUser(body)
            .enqueue(object : Callback<UserResponse> {
                override fun onResponse(
                    call: Call<UserResponse>,
                    response: Response<UserResponse>
                ) {
                    Log.d("Response", response.toString())
                    if (response.code() == 201) {
                        Log.d("Response", "_id: " + response.body()?._id.toString())
                        Log.d("Response", "name: " + response.body()?.name.toString())
                        Log.d("Response", "userId: " + response.body()?.userId.toString())
                        Log.d("Response", "isProvider: " + response.body()?.isProvider.toString())
                        Toast.makeText(
                            this@EditUserActivity,
                            "비밀번호가 성공적으로 변경되었습니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                        moveMainPage()
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    Log.d("Response", t.message.toString())
                }
            })
    }

    private fun moveMainPage() {
        startActivity(Intent(this, MainActivity::class.java))
    }
}