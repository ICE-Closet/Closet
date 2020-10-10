package com.example.smartice_closet

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
    private val USERGENDER = "USERGENDER"
    private val FIRST = "FIRSTCODY"
    private val SECOND = "SECONDCODY"
    private val THIRD = "THIRDCODY"

    var userColor = ""
    var userToken = ""
    var weather = ""
    var userGender = ""
    var userStyle = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_today_cody)

        userToken = intent.getStringExtra(TOKEN)
        weather = intent.getStringExtra(WEATHER)
        userGender = intent.getStringExtra(USERGENDER)

        colorPick_btn.setOnClickListener {
            openColorPicker()
        }

        style_dialog_btn.setOnClickListener {
            openstyleDialog()
        }

        recommend_btn.setOnClickListener {
            sendRecommendInfo(userColor, weather, userToken, userStyle)
        }
    }

    private fun openstyleDialog() {
        if (userGender == "M") {
            val maleStyleList = arrayOf("campus", "casual", "modern", "office", "simple", "travel")
            val MdialogBuilder = AlertDialog.Builder(this)
                .setTitle("Choose only one Fashion Style")

                .setSingleChoiceItems(maleStyleList, -1) { dialogInterface: DialogInterface?, i: Int ->
                    userStyle = maleStyleList[i]
                    setHashtag_tV.text = userStyle
                    dialogInterface?.dismiss()
                }

                .setNeutralButton("Cancel") {dialog: DialogInterface?, i: Int ->
                    dialog?.cancel()
                    Toast.makeText(this, "You must select a Fashion Hashtag", Toast.LENGTH_SHORT).show()
                }
            val m_hashtag_dialog = MdialogBuilder.create()
            m_hashtag_dialog.show()
        }

        else if (userGender == "F") {
            val femaleHashtagList = arrayOf("campus", "casual", "femi", "lovely", "modern", "office", "simple", "travel")
            val FdialogBuilder = AlertDialog.Builder(this)
                .setTitle("Choose only one Fashion Hashtag")

                .setSingleChoiceItems(femaleHashtagList, -1) { dialogInterface: DialogInterface?, i: Int ->
                    userStyle = femaleHashtagList[i]
                    setHashtag_tV.text = userStyle
                    dialogInterface?.dismiss()
                }

                .setNeutralButton("Cancel") {dialog: DialogInterface?, i: Int ->
                    dialog?.cancel()
                    Toast.makeText(this, "You must select a Fashion Hashtag", Toast.LENGTH_SHORT).show()
                }
            val f_hashtag_dialog = FdialogBuilder.create()
            f_hashtag_dialog.show()
        }

    }

    private fun sendRecommendInfo(userColor: String, weather: String, userToken: String, userStyle: String) {
        if (userColor != "" && userStyle != "") {
            val recommendRetrofit = Retrofit.Builder()
                .baseUrl("http://ec2-13-124-208-47.ap-northeast-2.compute.amazonaws.com:8000")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val userRecommendRequest: recommendRequest = recommendRetrofit.create(recommendRequest::class.java)

            userRecommendRequest.requestRecommend(token = userToken, userStyle = userStyle, userColor = userColor, weather = weather).enqueue(object : Callback<recommendResponse> {
                override fun onFailure(call: Call<recommendResponse>, t: Throwable) {
                    Log.e("onFailure", t.message)
                }

                override fun onResponse(call: Call<recommendResponse>, response: Response<recommendResponse>) {
                    if (response.isSuccessful) {
                        if (response.code() == 200) {
                            Log.d("Status 200", "Success")
                            var recommendResponse = response.body()



                            var firstCody = recommendResponse?.mediaUrl?.firstCody
                            var secondCody = recommendResponse?.mediaUrl?.secondCody
                            var thirdCody = recommendResponse?.mediaUrl?.thirdCody


                            Log.d("onResponse", recommendResponse?.msg)
                            Log.d("onResponse", recommendResponse?.mediaUrl.toString())

                            Log.d("first", firstCody.toString())
                            Log.d("second", secondCody.toString())
                            Log.d("third", thirdCody.toString())

                            if (firstCody != null && secondCody != null && thirdCody != null) {
                                Toast.makeText(applicationContext, "If you like our recommend, Choose them!!", Toast.LENGTH_SHORT).show()

                                var intent = Intent(applicationContext, recommend::class.java).apply {
                                    putExtra(TOKEN, userToken)
                                    putStringArrayListExtra(FIRST, firstCody as java.util.ArrayList<String>)
                                    putStringArrayListExtra(SECOND, secondCody as java.util.ArrayList<String>)
                                    putStringArrayListExtra(THIRD, thirdCody as ArrayList<String>)
                                }
                                startActivity(intent)
                                finish()
                            }
                            else {
                                Toast.makeText(applicationContext, "Not recommended", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    else { }
                }
            })
        }
        else {
            Toast.makeText(this, "You have to select both color and fashion style", Toast.LENGTH_SHORT).show()
        }

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
                    val colorArray = arrayOf("black", "white", "blue", "red", "gray", "yellow", "khaki", "beige", "pink", "purple")

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

