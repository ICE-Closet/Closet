package com.example.smartice_closet.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
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
import com.example.smartice_closet.main
import com.example.smartice_closet.todayCody
import com.example.smartice_closet.weather.weatherRequest
import com.example.smartice_closet.weather.weatherResponse
import kotlinx.android.synthetic.main.custom_alert_dialog_gender.*
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


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = arguments
        val userName = bundle!!.getString(USERNAME)
        val userToken = bundle!!.getString(TOKEN)
        var userGender = bundle!!.getString(USERGENDER)

        var realGender = ""

//        val dialogView = layoutInflater.inflate(R.layout.custom_alert_dialog_gender, null)
//        val genderPickBuilder = AlertDialog.Builder(context)
//            .setView(dialogView)
//
//        if (userGender == "N") {
//            dialogView.dialog_male_btn.setOnClickListener {
//                realGender = dialog_male_btn.text.toString()
//            }
//            dialogView.dialog_female_btn.setOnClickListener {
//                realGender = dialog_female_btn.text.toString()
//            }
//            genderPickBuilder.setPositiveButton("Save", DialogInterface.OnClickListener { dialog, which ->
//                val intent = Intent(context, main::class.java).apply {
//                    putExtra(USERGENDER, userGender)
//                }
//                startActivity(intent)
//                dialog.dismiss()
//            })
//            genderPickBuilder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->
//                val notCloseDialog: Boolean = false
//                if (notCloseDialog) {
//                    dialog.dismiss()
//                }
//            })
//            genderPickBuilder.show()
//        }
        Log.d("onViewCreated", userName + userToken)

        welcomeUser.text = "Welcome ${userName}"

        setWeather()

        todayCody_cV.setOnClickListener{
            val intent = Intent(context, todayCody::class.java)
            startActivity(intent)
        }

        camer_cV.setOnClickListener {
            val intent = Intent(context, camera::class.java).apply {
                putExtra(TOKEN, userToken)
            }
            startActivity(intent)
        }

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

                    weather.text = weatherAPIString
                }
            }

        })
    }
}

