package com.example.icecloset

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_menu.*

class menu : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        camera_btn.setOnClickListener {

        }

        photo_btn.setOnClickListener {
            var intent = Intent(applicationContext, photos::class.java)
            startActivity(intent)
        }

        recommend_btn.setOnClickListener {
            var intent = Intent(applicationContext, recommend::class.java)
            startActivity(intent)
        }
    }
}
