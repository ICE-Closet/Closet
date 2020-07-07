package com.example.icecloset

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_about_clothes.*

class aboutClothes : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_clothes)

        register_btn.setOnClickListener{
            var intent = Intent(applicationContext, camera::class.java)
            startActivity(intent)
        }
    }
}