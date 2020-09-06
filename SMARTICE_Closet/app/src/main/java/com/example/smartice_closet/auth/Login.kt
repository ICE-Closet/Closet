package com.example.smartice_closet.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.smartice_closet.R
import com.example.smartice_closet.fragments.homeFragment
import com.example.smartice_closet.main
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class Login : AppCompatActivity() {
    private val USERNAME = "USERNAME"
    private val TOKEN = "USERTOKEN"

    var userToken: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val signUp_Tbtn = findViewById(R.id.signUp) as TextView
//        val password_Tbtn = findViewById(R.id.forgotPwd) as TextView  // Not Implemented

        val retrofit = Retrofit.Builder()
            .baseUrl("http://ec2-13-124-208-47.ap-northeast-2.compute.amazonaws.com:8000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var loginservice: loginRequest = retrofit.create(loginRequest::class.java)

        login_btn.setOnClickListener {
            var s_email = inputEmail.text.toString()
            var s_pw = inputPwd.text.toString()

            loginservice.requestLogin(s_email, s_pw).enqueue(object : Callback<loginResponse> {
                override fun onFailure(call: Call<loginResponse>, t: Throwable) {
                    Log.e("Login", t.message)
                    var dialog = AlertDialog.Builder(this@Login)
                    dialog.setTitle("ERROR")
                    dialog.setMessage("서버와의 통신에 실패하였습니다.")
                    dialog.show()
                }

                override fun onResponse(call: Call<loginResponse>, response: Response<loginResponse>) {
                    if (response?.isSuccessful) {
                        var login_response = response.body()
                        Log.d("SUCCESS", login_response?.code)

                        val code = login_response?.code?.let { it -> Integer.parseInt(it) }
                        if (code == 201) {
                            Toast.makeText(this@Login, "로그인에 성공하였습니다. \n 즐거운 하루 되세요.", Toast.LENGTH_SHORT).show()
                            var intent = Intent(applicationContext, main::class.java).apply {
                                putExtra(TOKEN, login_response?.token)
                            }
                            startActivity(intent)
                            finish()
                        }
                        else if (code == 0) {
                            Toast.makeText(this@Login, "이메일이 인증되지 않았습니다. \n 입력하신 이메일로 인증해 주세요.", Toast.LENGTH_SHORT).show()
                        }
                        else if (code == 1) {
                            Toast.makeText(this@Login, "로그인에 실패하였습니다. \n 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show()
                        }
                        else if (code == 2) {
                            Toast.makeText(this@Login, "회원가입 후 로그인 해주세요.", Toast.LENGTH_SHORT).show()

                        }
                    }
                    else {
                        var login_response = response.body()
                        Log.e("FAIL", response.errorBody().toString())
                        Log.d("FAIL", login_response?.code)
                        Toast.makeText(this@Login, "로그인에 실패하였습니다. \n 계정 혹은 비밀번호를 다시 확인하세요.", Toast.LENGTH_SHORT).show()
                    }
                }

            })

        }

        signUp_Tbtn.setOnClickListener {
            var intent = Intent(this, signup::class.java)
            startActivity(intent)
            finish()
        }

    }
}