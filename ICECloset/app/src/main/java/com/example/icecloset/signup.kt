package com.example.icecloset

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_signup.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class signup : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

//        var retrofit = Retrofit.Builder()
//            .baseUrl("http://192.168.0.10:88")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//
//        var signupservice: forSignupService = retrofit.create(forSignupService::class.java)

        complete_btn.setOnClickListener {
//            var s_name = signup_name.text.toString()
//            var s_email = signup_email.text.toString()
//            var s_pw1 = signup_pw1.text.toString()
//            var s_pw2 = signup_pw2.text.toString()
//
//            if (s_pw1 == s_pw2) {
//                Log.d("CHECK", "같음!!")
//                var intent = Intent(applicationContext, login::class.java)
//                startActivity(intent)
//                signupservice.requestSignup(s_name, s_email, s_pw1).enqueue(object : Callback<forSignup> {
//                    override fun onFailure(call: Call<forSignup>, t: Throwable) {
//                        Log.e("회원가입", t.message)
//                        var dialog = AlertDialog.Builder(this@signup)
//                        dialog.setTitle("Error")
//                        dialog.setMessage("서버와의 통신이 실패하였습니다.")
//                        dialog.show()
//                    }
//
//                    override fun onResponse(call: Call<forSignup>, response: Response<forSignup>) {
//                        if (response?.isSuccessful) {
//                            var forSignup = response.body()
//                            Log.d("SIGNUP", "msg : " + forSignup?.msg)
//                            Log.d("SIGNUP", "code : " + forSignup?.code)
//                            var dialog = AlertDialog.Builder(this@signup)
//                            Toast.makeText(this@signup, "회원가입이 완료되었습니다. \n 사용자 이메일 : " + s_email, Toast.LENGTH_LONG).show()
//                            var intent = Intent(applicationContext, login::class.java)
//                            startActivity(intent)
//                            finish()
//                        }
//                        else {
//                            var forSignup = response.body()
//                            Log.d("ERROR", forSignup?.code)
//                            Toast.makeText(this@signup, "회원가입이 실패하였습니다.", Toast.LENGTH_SHORT).show()
//
//                        }
//                    }
//                })
//            }
//            else {
//
//                var dialog = AlertDialog.Builder(this@signup)
//                dialog.setTitle("회원가입 실패")
//                dialog.setMessage("비밀 번호를 확인하세요.")
//                dialog.show()
//            }
            var intent = Intent(applicationContext, login::class.java)
            startActivity(intent)
            finish()

        }
    }
}
