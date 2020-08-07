package com.example.icecloset

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.icecloset.auth.login
import com.example.icecloset.weather.WeatherResponse
import com.example.icecloset.weather.WeatherService
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main_view.*
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.roundToInt

class mainView : AppCompatActivity() {

    private val TOKEN = "USERTOKEN"

    companion object {
        fun getLaunchIntent(from: Context) = Intent(from, mainView::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
    }

    /* openWeatherAPI */
    var BaseURL = "http://api.openweathermap.org/"
    var AppKey = "1cdf1f631d32bf81a63275b6486282f4"
    var lat = "37.57"
    var lon = "126.98"  // Seoul

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_view)

        val retrofit = Retrofit.Builder()
            .baseUrl(BaseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val weatherService = retrofit.create(WeatherService::class.java)
        val call = weatherService.getCurrentWeatherData(lat, lon, AppKey)
        call.enqueue(object : Callback<WeatherResponse> {
            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Log.d("FAILED", "message : " + t.message)
            }

            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.code() == 200) {
                    val weatherResponse = response.body()
                    Log.d("SUCCESS", "messsage : " + weatherResponse?.code)
                    var Temperature = weatherResponse!!.main!!.temp - 273.15
                    var min_Temp = weatherResponse!!.main!!.temp_min - 273.15
                    var max_Temp = weatherResponse!!.main!!.temp_max - 273.15
                    var ave_Temp = (min_Temp + max_Temp) / 2

                    var r_Temp = Temperature.roundToInt()
//                    var r_minTemp = min_Temp.roundToInt()
//                    var r_maxTemp = max_Temp.roundToInt()
                    var r_aveTemp = ave_Temp.roundToInt()

                    val weatherAPIString = "국가 : " + weatherResponse!!.sys!!.country + "\n" + "현재 기온 : " + r_Temp + " " + "평균 기온 : " + r_aveTemp
                    weatherAPI.text = weatherAPIString
                }
            }
        })

        var userToken = intent.getStringExtra(TOKEN).toString()
        Log.d(TOKEN, userToken)

        initializeUI() // Google Auth


        user_closet.setOnClickListener {
            var intent = Intent(applicationContext, aboutClothes::class.java).apply {
                putExtra(TOKEN, userToken)
            }
            startActivity(intent)
        }

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
