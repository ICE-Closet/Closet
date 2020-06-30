package com.example.icecloset

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock

class splashLoading : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        SystemClock.sleep(500)
        val intent = Intent(this, login::class.java)
        startActivity(intent)
        finish()
    }
}
