package com.example.icecloset

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.icecloset.auth.login
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class main : AppCompatActivity() {

    private val TOKEN = "USERTOKEN"

    companion object {
        fun getLaunchIntent(from: Context) = Intent(from, main::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        var userToken = intent.getStringExtra(TOKEN).toString()
//        Log.d(TOKEN, userToken)

        initializeUI() // Google Auth


//        user_closet.setOnClickListener {
//            var intent = Intent(applicationContext, aboutClothes::class.java).apply {
//                putExtra(TOKEN, userToken)
//            }
//            startActivity(intent)
//        }

        user_cody.setOnClickListener {
            var intent = Intent(applicationContext, aboutClothes::class.java)
            startActivity(intent)
        }
    }

    private fun initializeUI() {
        logout_btn.setOnClickListener {
            logout()
        }
    }

    private fun logout() {
        startActivity(login.getLaunchIntent(this))
        FirebaseAuth.getInstance().signOut()
    }
}