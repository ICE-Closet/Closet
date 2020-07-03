package com.example.icecloset

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        var retrofit = Retrofit.Builder()
            .baseUrl("")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var loginservice: forSignupService = retrofit.create(forSignupService::class.java)

        login_btn.setOnClickListener {
            var intent = Intent(applicationContext, menu::class.java)
            startActivity(intent)
        }

        signup_btn.setOnClickListener {
            var intent = Intent(applicationContext, signup::class.java)
            startActivity(intent)
        }
    }


}
