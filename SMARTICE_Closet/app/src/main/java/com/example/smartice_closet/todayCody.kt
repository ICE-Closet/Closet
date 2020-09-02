package com.example.smartice_closet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_today_cody.*
import petrov.kristiyan.colorpicker.ColorPicker

class todayCody : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_today_cody)

        colorPick_btn.setOnClickListener {
            openColorPicker()
        }

        // Retrofit to Server : Token, Weather, Color, Hashtag
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

                    setColor_tV.text = colorArray[position]
                }

                override fun onCancel() {  }
            }).show()
    }
}

