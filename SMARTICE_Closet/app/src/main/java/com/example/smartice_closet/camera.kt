package com.example.smartice_closet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import kotlinx.android.synthetic.main.activity_camera.*
import java.text.SimpleDateFormat
import java.util.*

class camera : AppCompatActivity() {

    private val TOKEN = "USERTOKEN"
    var userTimeStamp: String = ""

    val REQUEST_IMAGE_CAPTURE = 1 // for Camera Permission code
    lateinit var curPhotoPath: String

    var userToken: String = ""

    var timestamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    var fileName = "${timestamp}.jpeg"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        setPermission()

        capture_btn.setOnClickListener {
            takeCapture()
        }

        save_btn.setOnClickListener {
            sendPhoto()
        }


    }

    private fun takeCapture() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    }
}