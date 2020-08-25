package com.example.smartice_closet.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.smartice_closet.R
import kotlinx.android.synthetic.main.activity_signup.*

class signup : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        submit_btn.setOnClickListener {     // Retrofit 구현 필요
            var intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }
    }
}