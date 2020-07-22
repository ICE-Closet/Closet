package com.example.icecloset

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.icecloset.camera.camera
import kotlinx.android.synthetic.main.activity_about_clothes.*

class aboutClothes : AppCompatActivity() {

    private val TOKEN = "USERTOKEN"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_clothes)

        var userToken = intent.getStringExtra(TOKEN).toString()
        Log.d(TOKEN, userToken)

        register_btn.setOnClickListener{
            var intent = Intent(applicationContext, camera::class.java).apply {
                putExtra(TOKEN, userToken)
            }
            startActivity(intent)
        }
    }
}