package com.example.smartice_closet.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.smartice_closet.R
import com.example.smartice_closet.camera
import com.example.smartice_closet.homeDialog.homeDialogRequest
import com.example.smartice_closet.homeDialog.homeDialogResponse
import com.example.smartice_closet.main
import com.example.smartice_closet.todayCody
import com.example.smartice_closet.weather.weatherRequest
import com.example.smartice_closet.weather.weatherResponse
import kotlinx.android.synthetic.main.custom_alert_dialog_gender.view.*
import kotlinx.android.synthetic.main.custom_alert_dialog_gender.view.dialog_female_btn
import kotlinx.android.synthetic.main.fragment_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.roundToInt

/**
 * A simple [Fragment] subclass.
 * Use the [homeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class homeFragment : Fragment() {

    private val USERNAME = "USERNAME"
    private val TOKEN = "USERTOKEN"
    private val USERGENDER = "USERGENDER"
    private val WEATHER = "WEATHER"

    var googleGender = ""
    var userToken = ""
    var userName = ""
    var userGender = ""

    var modifiedGender = ""
    var todayWeather = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = arguments
        userName = bundle!!.getString(USERNAME).toString()
        userToken = bundle!!.getString(TOKEN).toString()
        userGender = bundle!!.getString(USERGENDER).toString()

        Log.d("onViewCreated", userName + userToken)
        Log.d("onViewCreated", userGender)

        setDialogForGoogle(userGender)

        welcomeUser.text = "Welcome ${userName}"

        setWeather()

        todayCody_cV.setOnClickListener{
            val intent = Intent(context, todayCody::class.java).apply {
                putExtra(TOKEN, userToken)
                putExtra(WEATHER, todayWeather)
                putExtra(USERGENDER, userGender)
            }
            startActivity(intent)
        }

        camer_cV.setOnClickListener {
            val intent = Intent(context, camera::class.java).apply {
                putExtra(TOKEN, userToken)
            }
            startActivity(intent)
        }

    }

    private fun setDialogForGoogle(userGenders: String?) {
        if (userGenders == "N") {
            val dialogView = LayoutInflater.from(context).inflate(R.layout.custom_alert_dialog_gender, null)
            val dialogBuilder = AlertDialog.Builder(context)
                .setView(dialogView)
                .setMessage("Please select your gender.")

            val googleDialog = dialogBuilder.show()

            dialogView.dialog_male_btn.setOnClickListener {
                userGender = dialogView.dialog_male_btn.text.toString()
                googleDialog.dismiss()
                Log.d(USERGENDER, userGender)

                sendGenderToServer(userToken, userGender)

                val intent = Intent(context, main::class.java).apply {
                    putExtra(TOKEN, userToken)
                    putExtra(USERNAME, userName)
                    putExtra(USERGENDER, modifiedGender)
                }
                startActivity(intent)
            }

            dialogView.dialog_female_btn.setOnClickListener {
                userGender = dialogView.dialog_female_btn.text.toString()
                googleDialog.dismiss()
                Log.d(USERGENDER, userGender)

                sendGenderToServer(userToken, userGender)

                val intent = Intent(context, main::class.java).apply {
                    putExtra(TOKEN, userToken)
                    putExtra(USERNAME, userName)
                    putExtra(USERGENDER, modifiedGender)
                }
                startActivity(intent)
            }
        }
    }

    private fun sendGenderToServer(userToken: String, userGender: String) {
        val newGoogleRetrofit = Retrofit.Builder()
            .baseUrl("http://ec2-13-124-208-47.ap-northeast-2.compute.amazonaws.com:8000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val googleGenderRequest: homeDialogRequest = newGoogleRetrofit.create(homeDialogRequest::class.java)

        googleGenderRequest.requestHomeDialog(userToken = userToken, userGender = userGender).enqueue(object : Callback<homeDialogResponse> {
            override fun onFailure(call: Call<homeDialogResponse>, t: Throwable) {
                Log.e("onFailure", t.message)
            }

            override fun onResponse(call: Call<homeDialogResponse>, response: Response<homeDialogResponse>) {
                if (response.isSuccessful) {
                    if (response.code() == 200) {
                        Log.d("Status 200", "Success")
                        var dialog_Response = response.body()

                        Toast.makeText(context, "Successfully saved", Toast.LENGTH_SHORT).show()
                        Log.d("onResponse", dialog_Response?.msg)
                        Log.d("onResponse", dialog_Response?.sex)

                        modifiedGender = dialog_Response?.sex.toString()
                    }
                }
                else {   }
            }
        })
    }

    private fun setWeather() {
        /* openWeatherAPI */
        val BaseURL = "http://api.openweathermap.org/"
        val AppKey = "1cdf1f631d32bf81a63275b6486282f4"
        val lat = "37.57"
        val lon = "126.98"  // Seoul

        val retrofit = Retrofit.Builder()
            .baseUrl(BaseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val weatherService = retrofit.create(weatherRequest::class.java)
        val call = weatherService.getCurrentWeatherData(lat, lon, AppKey)
        
        call.enqueue(object : Callback<weatherResponse> {
            override fun onFailure(call: Call<weatherResponse>, t: Throwable) {
                Log.d("onFailure(Weather)", "message : " + t.message)
            }

            override fun onResponse(call: Call<weatherResponse>, response: Response<weatherResponse>) {
                if (response.code() == 200) {
                    val weatherResponse = response.body()
                    Log.d("onResponse(Weather)", "messsage : " + weatherResponse?.code)
                    var Temperature = weatherResponse!!.main!!.temp - 273.15
                    var min_Temp = weatherResponse!!.main!!.temp_min - 273.15
                    var max_Temp = weatherResponse!!.main!!.temp_max - 273.15
                    var ave_Temp = (min_Temp + max_Temp) / 2

                    var r_Temp = Temperature.roundToInt()
//                    var r_minTemp = min_Temp.roundToInt()
//                    var r_maxTemp = max_Temp.roundToInt()
                    var r_aveTemp = ave_Temp.roundToInt()

                    val weatherAPIString = "Nation : " + weatherResponse!!.sys!!.country + "\n" + "Temperature : " + r_Temp + "\n" + "Average Temperature : " + r_aveTemp

                    todayWeather = r_Temp.toString()
                    weather.text = weatherAPIString
                }
            }

        })
    }
}

