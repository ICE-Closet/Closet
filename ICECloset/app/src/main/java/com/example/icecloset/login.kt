package com.example.icecloset

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_login.*

class login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        signup_btn.setOnClickListener {
            var intent = Intent(applicationContext, signup::class.java)
            startActivity(intent)
        }
    }


}
