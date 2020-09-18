package com.example.smartice_closet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.smartice_closet.recommendForUser.recommend
import com.example.smartice_closet.recommendForUser.recommendRequest
import kotlinx.android.synthetic.main.activity_today_cody.*
import petrov.kristiyan.colorpicker.ColorPicker
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class todayCody : AppCompatActivity() {

    private val TOKEN = "USERTOKEN"
    private val WEATHER = "WEATHER"

    var userColor = ""
    var userToken = ""
    var weather = ""
    var userHashtag = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_today_cody)

        userToken = intent.getStringExtra(TOKEN)
        weather = intent.getStringExtra(WEATHER)

        colorPick_btn.setOnClickListener {
            openColorPicker()
        }

        recommend_btn.setOnClickListener {
            sendRecommendInfo(userColor)
        }
        // Retrofit to Server : Token, Weather, Color, Hashtag
    }

    private fun sendRecommendInfo(userColor: String) {
//        val recommendRetrofit = Retrofit.Builder()
//            .baseUrl("http://ec2-13-124-208-47.ap-northeast-2.compute.amazonaws.com:8000")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//
//        val recommendService: recommendRequest = recommendRetrofit.create(recommendRequest::class.java)
//
//        recommendService.requestRecommend()
        var intent = Intent(applicationContext, recommend::class.java).apply {
            putExtra(TOKEN, userToken)
        }
        startActivity(intent)
        finish()
    }

    private fun openColorPicker() {
        val colorPicker = ColorPicker(this)
        val color = ArrayList<String>()

        color.add("#000000")
        color.add("#FFFFFF")
        color.add("#000CFF")
        color.add("#FF0000")
        color.add("#A0A0A0")
        color.add("#EFFF00")
        color.add("#27563D")
        color.add("#BA9A82")

        colorPicker.setColors(color)
            .setTitle("Pick One Color")
            .setColumns(4)
            .setRoundColorButton(true)
            .setOnChooseColorListener(object : ColorPicker.OnChooseColorListener {
                override fun onChooseColor(position: Int, color: Int) {
                    val colorArray = arrayOf("black", "white", "blue", "red", "gray", "yellow", "khaki", "beige")

//                    if (position == null) {       // 색 안눌렀을때 꺼지는거 예외처리 해볼래? 시름말구
//                        Toast.makeText(applicationContext, "You have to pick one color", Toast.LENGTH_SHORT).show()
//                    }

                    userColor = colorArray[position]
                    setColor_tV.text = userColor
                }

                override fun onCancel() {  }
            }).show()
    }
}

