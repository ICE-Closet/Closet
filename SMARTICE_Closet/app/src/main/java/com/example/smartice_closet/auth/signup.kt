package com.example.smartice_closet.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.smartice_closet.R
import kotlinx.android.synthetic.main.activity_signup.*
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class signup : AppCompatActivity() {
    private val USERNAME = "USERNAME"
    var s_gender : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://ec2-13-124-208-47.ap-northeast-2.compute.amazonaws.com:8000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var signupservice: signupRequest = retrofit.create(signupRequest::class.java)

        sexGroup.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.choiceMale) {
                s_gender = choiceMale.text.toString()
                Log.d("submit_btn(Male)", s_gender)
            }
            else if (checkedId == R.id.choiceFemale) {
                s_gender = choiceFemale.text.toString()
                Log.d("submit_btn(Female)", s_gender)
            }
        }

        submit_btn.setOnClickListener {
            var s_email = inputEmail_signUp.text.toString()
            var s_pwd1 = inputPassword_signUp.text.toString()
            var s_pwd2 = inputPasswordAgain.text.toString()
            var s_name = inputName.text.toString()

            Log.d("submit_btn(gender)", s_gender)


            if (s_pwd1 == s_pwd2) {
                Log.d("setOnClickListner", "Same Pwd!")

                signupservice.requestSignup(s_name, s_email, s_pwd1, s_gender).enqueue(object : Callback<signupResponse> {
                    override fun onFailure(call: Call<signupResponse>, t: Throwable) {
                        Log.e("회원가입", t.message)
                        var dialog = AlertDialog.Builder(this@signup)
                        dialog.setTitle("Error")
                        dialog.setMessage("서버와의 통신이 실패하였습니다.")
                        dialog.show()
                    }

                    override fun onResponse(call: Call<signupResponse>, response: Response<signupResponse>) {
                        if (response?.isSuccessful) {
                            var signup_response = response.body()
                            Log.d("SIGNUP", "msg : " + signup_response?.msg)
                            Log.d("SIGNUP", "code : " + signup_response?.code)

                            Toast.makeText(this@signup, "회원가입이 완료되었습니다. \n 사용자 이메일 : " + s_email, Toast.LENGTH_LONG).show()
                            var intent = Intent(applicationContext, Login::class.java).apply {
                                putExtra(USERNAME, s_name)
                            }
                            startActivity(intent)
                            finish()
                        }
                        else {
                            var signup_response = response.body()
                            Log.d("ERROR", signup_response?.code)
                            Toast.makeText(this@signup, "회원가입이 실패하였습니다.", Toast.LENGTH_SHORT).show()

                        }
                    }

                })
            }
            else {
                Toast.makeText(this, "비밀번호가 일치하지 않습니다.\n다시 입력하세요", Toast.LENGTH_SHORT).show()
            }
        }
    }
}