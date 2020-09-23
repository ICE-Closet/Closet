package com.example.smartice_closet

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartice_closet.recommendForUser.recommend
import com.example.smartice_closet.recommendForUser.recommendRequest
import com.example.smartice_closet.recommendForUser.recommendResponse
import kotlinx.android.synthetic.main.activity_today_cody.*
import petrov.kristiyan.colorpicker.ColorPicker
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
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

        radioListner()

        recommend_btn.setOnClickListener {
            sendRecommendInfo(userColor, weather, userToken, userHashtag)
        }
    }

    private fun radioListner(view: View) {
        when (view.id) {
            R.id.a_btn -> {
                radioGroup2.clearCheck()
                val radioBtnId : View = radioGroup1.findViewById(radioGroup1.checkedRadioButtonId)
                val radioIndex : Int = radioGroup1.indexOfChild(radioBtnId)
                val realBtn = radioGroup1.getChildAt(radioIndex) as RadioButton
                userHashtag = realBtn.text.toString()
            }
            R.id.b_btn -> {
                radioGroup2.clearCheck()
                val radioBtnId : View = radioGroup1.findViewById(radioGroup1.checkedRadioButtonId)
                val radioIndex : Int = radioGroup1.indexOfChild(radioBtnId)
                val realBtn = radioGroup1.getChildAt(radioIndex) as RadioButton
                userHashtag = realBtn.text.toString()
            }
            R.id.c_btn -> {
                radioGroup1.clearCheck()
                val radioBtnId : View = radioGroup2.findViewById(radioGroup1.checkedRadioButtonId)
                val radioIndex : Int = radioGroup2.indexOfChild(radioBtnId)
                val realBtn = radioGroup2.getChildAt(radioIndex) as RadioButton
                userHashtag = realBtn.text.toString()
            }
            R.id.d_btn -> {
                radioGroup1.clearCheck()
                val radioBtnId : View = radioGroup2.findViewById(radioGroup1.checkedRadioButtonId)
                val radioIndex : Int = radioGroup2.indexOfChild(radioBtnId)
                val realBtn = radioGroup2.getChildAt(radioIndex) as RadioButton
                userHashtag = realBtn.text.toString()
            }
        }
    }

    fun onClick(v: View) {
        // TODO Auto-generated method stub
        when (v.id) {
            R.id.btn_id_confirm -> {
                var selectedResult = ""
                if (mRgLine1.getCheckedRadioButtonId() > 0) {
                    val radioButton: View =
                        mRgLine1.findViewById(mRgLine1.getCheckedRadioButtonId())
                    val radioId: Int = mRgLine1.indexOfChild(radioButton)
                    val btn = mRgLine1.getChildAt(radioId) as RadioButton
                    selectedResult = btn.text as String
                } else if (mRgLine2.getCheckedRadioButtonId() > 0) {
                    val radioButton: View =
                        mRgLine2.findViewById(mRgLine2.getCheckedRadioButtonId())
                    val radioId: Int = mRgLine2.indexOfChild(radioButton)
                    val btn = mRgLine2.getChildAt(radioId) as RadioButton
                    selectedResult = btn.text as String
                }
                Toast.makeText(applicationContext, selectedResult, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendRecommendInfo(userColor: String, weather: String, userToken: String, userHashtag: String) {
        val recommendRetrofit = Retrofit.Builder()
            .baseUrl("http://ec2-13-124-208-47.ap-northeast-2.compute.amazonaws.com:8000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val userRecommendRequest: recommendRequest = recommendRetrofit.create(recommendRequest::class.java)

        userRecommendRequest.requestRecommend(token = userToken, userHashtag = userHashtag, userColor = userColor, weather = weather).enqueue(object : Callback<recommendResponse> {
            override fun onFailure(call: Call<recommendResponse>, t: Throwable) {
                Log.e("onFailure", t.message)
            }

            override fun onResponse(call: Call<recommendResponse>, response: Response<recommendResponse>) {
                if (response.isSuccessful) {
                    if (response.code() == 200) {
                        Log.d("Status 200", "Success")
                        var recommendResponse = response.body()

                        Toast.makeText(applicationContext, "Successfully recommended\nIf you like our recommend, Choose them!!", Toast.LENGTH_SHORT).show()
                        Log.d("onResponse", recommendResponse?.msg)
                        Log.d("onResponse", recommendResponse?.mediaUrl.toString())
                        Log.d("first", recommendResponse?.mediaUrl?.firstCody.toString())
                        Log.d("second", recommendResponse?.mediaUrl?.secondCody.toString())
                        Log.d("third", recommendResponse?.mediaUrl?.thirdCody.toString())

                        var intent = Intent(applicationContext, recommend::class.java).apply {
                            putExtra(TOKEN, userToken)
                        }
                        startActivity(intent)
                        finish()
                    }
                }
                else { }
            }
        })
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

