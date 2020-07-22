package com.example.icecloset.auth

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.icecloset.R
import com.example.icecloset.auth.kakao.SessionCallback
import com.example.icecloset.main
import com.example.icecloset.socialAuth
import com.kakao.auth.Session
import com.kakao.util.helper.Utility.getKeyHash
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class login : AppCompatActivity() {
    private val TOKEN = "USERTOKEN"

    private var callback : SessionCallback = SessionCallback()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        Session.getCurrentSession().addCallback(callback)

        var retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.0.10:88")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var loginservice: forLoginService = retrofit.create(
            forLoginService::class.java)

        login_btn.setOnClickListener {
            var s_email = user_email.text.toString()
            var s_pw = user_pw.text.toString()

            loginservice.requestLogin(s_email,s_pw).enqueue(object :Callback<forLogin>{
                override fun onFailure(call: Call<forLogin>, t: Throwable) {
                    Log.e("Login", t.message)
                    var dialog = AlertDialog.Builder(this@login)
                    dialog.setTitle("ERROR")
                    dialog.setMessage("서버와의 통신에 실패하였습니다.")
                    dialog.show()
                }

                override fun onResponse(call: Call<forLogin>, response: Response<forLogin>) {
                    if (response?.isSuccessful) {
                        var forLogin = response.body()
                        Log.d("SUCCESS", forLogin?.code)
                        val code = forLogin?.code?.let { it1 -> Integer.parseInt(it1) }
                        if (code == 201) {
                            Toast.makeText(this@login, "로그인에 성공하였습니다. \n 즐거운 하루 되세요.", Toast.LENGTH_SHORT).show()
                            var intent = Intent(applicationContext, main::class.java).apply {
                                putExtra(TOKEN, forLogin?.token)
                            }
                            startActivity(intent)
                        }
                        else if (code == 0) {
                            Toast.makeText(this@login, "이메일이 인증되지 않았습니다. \n 입력하신 이메일로 인증해 주세요.", Toast.LENGTH_SHORT).show()
                        }
                        else if (code == 1) {
                            Toast.makeText(this@login, "로그인에 실패하였습니다. \n 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show()
                        }
                        else if (code == 2) {
                            Toast.makeText(this@login, "회원가입 후 로그인 해주세요.", Toast.LENGTH_SHORT).show()
                        }

//                        var intent = Intent(applicationContext, main::class.java)
//                        startActivity(intent)
                    }
                    else {
                        var forLogin = response.body()
                        Log.d("FAIL", forLogin?.code)
                        Toast.makeText(this@login, "로그인에 실패하였습니다. \n 계정 혹은 비밀번호를 다시 확인하세요.", Toast.LENGTH_SHORT).show()
                    }
                }
            })
//            var intent = Intent(applicationContext, main::class.java)
//            startActivity(intent)


        }

        signup_btn.setOnClickListener {
            var intent = Intent(applicationContext, signup::class.java)
            startActivity(intent)
        }
    }

    // Kakao
    @SuppressLint("MissingSuperCall")
    override fun onDestroy() {
        Session.getCurrentSession().removeCallback(callback)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            Log.i("Log", "Session get current session")
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}


