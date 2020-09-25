package com.example.smartice_closet

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartice_closet.recommendForUser.recommend
import com.example.smartice_closet.recommendForUser.recommendRequest
import com.example.smartice_closet.recommendForUser.recommendResponse
import kotlinx.android.synthetic.main.activity_today_cody.*
import kotlinx.android.synthetic.main.custom_dialog_female_hashtag.*
import kotlinx.android.synthetic.main.custom_dialog_male_hashtag.*
import kotlinx.android.synthetic.main.custom_dialog_male_hashtag.campus_rbtn
import kotlinx.android.synthetic.main.custom_dialog_male_hashtag.casual_rbtn
import kotlinx.android.synthetic.main.custom_dialog_male_hashtag.modern_rbtn
import kotlinx.android.synthetic.main.custom_dialog_male_hashtag.office_rbtn
import kotlinx.android.synthetic.main.custom_dialog_male_hashtag.simple_rbtn
import kotlinx.android.synthetic.main.custom_dialog_male_hashtag.travel_rbtn
import kotlinx.android.synthetic.main.custom_dialog_male_hashtag.view.*
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
    private val USERHASHTAG = "USERHASHTAG"

    var userColor = ""
    var userToken = ""
    var weather = ""
    var userGender = ""
    var userHashtag = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_today_cody)

        userToken = intent.getStringExtra(TOKEN)
        weather = intent.getStringExtra(WEATHER)
        userGender = intent.getStringExtra(USERGENDER)

        colorPick_btn.setOnClickListener {
            openColorPicker()
        }

        hashtag_dialog_btn.setOnClickListener {
            openHashtagDialog()
        }

        recommend_btn.setOnClickListener {
            sendRecommendInfo(userColor, weather, userToken, userHashtag)
        }
    }

    private fun openHashtagDialog() {
        if (userGender == "M") {
            val maleHashtagList = arrayOf("campus", "casual", "modern", "office", "simple", "travel")
            val MdialogBuilder = AlertDialog.Builder(this)
                .setTitle("Choose only one Fashion Hashtag")

                .setSingleChoiceItems(maleHashtagList, -1) { dialogInterface: DialogInterface?, i: Int ->
                    userHashtag = maleHashtagList[i]
                    setHashtag_tV.text = userHashtag
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
            val femaleHashtagList = arrayOf("campus", "casual", "femi", "lovely", "modern", "ofice", "simple", "travel")
            val FdialogBuilder = AlertDialog.Builder(this)
                .setTitle("Choose only one Fashion Hashtag")

                .setSingleChoiceItems(femaleHashtagList, -1) { dialogInterface: DialogInterface?, i: Int ->
                    userHashtag = femaleHashtagList[i]
                    setHashtag_tV.text = userHashtag
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

    private fun sendRecommendInfo(userColor: String, weather: String, userToken: String, userHashtag: String) {
        if (userColor != "" && userHashtag != "") {
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
        else {
            Toast.makeText(this, "You have to select both color and fashion hashtag", Toast.LENGTH_SHORT).show()
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

