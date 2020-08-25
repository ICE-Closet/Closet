package com.example.smartice_closet.auth

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.smartice_closet.R
import com.example.smartice_closet.main
import kotlinx.android.synthetic.main.activity_login.*


class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val signUp_Tbtn = findViewById(R.id.signUp) as TextView
//        val password_Tbtn = findViewById(R.id.forgotPwd) as TextView  // Not Implemented



        login_btn.setOnClickListener { // Retrofit 구현 필요
            var intent = Intent(this, main::class.java)
            startActivity(intent)
            finish()
        }

        signUp_Tbtn.setOnClickListener {
            var intent = Intent(this, signup::class.java)
            startActivity(intent)
            finish()
        }

    }
}