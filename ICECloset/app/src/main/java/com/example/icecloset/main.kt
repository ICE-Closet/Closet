package com.example.icecloset

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class main : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        user_closet.setOnClickListener {
            var intent = Intent(applicationContext, aboutClothes::class.java)
            startActivity(intent)
        }

        user_cody.setOnClickListener {
            var intent = Intent(applicationContext, aboutClothes::class.java)
            startActivity(intent)
        }
    }
}