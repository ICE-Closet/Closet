package com.example.smartice_closet.recommendForUser

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import com.example.smartice_closet.R
import com.example.smartice_closet.adapters.recommend1Adapter
import com.example.smartice_closet.adapters.recommend2Adapter
import com.example.smartice_closet.adapters.recommend3Adapter
import com.example.smartice_closet.models.recommend1
import com.example.smartice_closet.models.recommend2
import com.example.smartice_closet.models.recommend3
import com.example.smartice_closet.selectCody.selectCodyRequest
import com.example.smartice_closet.selectCody.selectCodyResponse
import kotlinx.android.synthetic.main.activity_recommend.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.StringBuilder


class recommend : AppCompatActivity() {

    private val TOKEN = "USERTOKEN"
    private val FIRST = "FIRSTCODY"
    private val SECOND = "SECONDCODY"
    private val THIRD = "THIRDCODY"

    var userToken = ""

    lateinit var firstCody : ArrayList<String>
    lateinit var secondCody : ArrayList<String>
    lateinit var thirdCody : ArrayList<String>



    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recommend)

        userToken = intent.getStringExtra(TOKEN)
        Log.d(TOKEN, userToken)

        firstCody = intent.getStringArrayListExtra(FIRST)
        Log.d(FIRST, firstCody.toString())

        secondCody = intent.getStringArrayListExtra(SECOND)
        Log.d(SECOND, secondCody.toString())

        thirdCody = intent.getStringArrayListExtra(THIRD)
        Log.d(THIRD, thirdCody.toString())

        recommend1_select_btn.setOnClickListener {
            sendFirstCody()
        }

        recommend2_select_btn.setOnClickListener {
            sendSecondCody()
        }

        recommend3_select_btn.setOnClickListener {
            sendThirdCody()
        }

//        val cody1List = arrayListOf<recommend1>()
//        for (i in firstCody) {
//            Log.d("I", i)
//            cody1List.add(i)
//        }

        val cody1List = arrayListOf<recommend1>()
        for (i in firstCody.indices) {
            cody1List.add(recommend1(firstCody[i]))
        }

        Log.d("CodyList", cody1List.toString())

        val cody2List = arrayListOf<recommend2>()
        for (i in secondCody.indices) {
            cody2List.add(recommend2(secondCody[i]))
        }

        Log.d("CodyList", cody2List.toString())

        val cody3List = arrayListOf<recommend3>()
        for (i in thirdCody.indices) {
            cody3List.add(recommend3(thirdCody[i]))
        }

        Log.d("CodyList", cody3List.toString())

        firstCody_RV.layoutManager = LinearLayoutManager(this, OrientationHelper.HORIZONTAL, false)
        firstCody_RV.setHasFixedSize(true)
        firstCody_RV.adapter = recommend1Adapter(cody1List)

        secondCody_RV.layoutManager = LinearLayoutManager(this, OrientationHelper.HORIZONTAL, false)
        secondCody_RV.setHasFixedSize(true)
        secondCody_RV.adapter = recommend2Adapter(cody2List)

        thirdCody_RV.layoutManager = LinearLayoutManager(this, OrientationHelper.HORIZONTAL, false)
        thirdCody_RV.setHasFixedSize(true)
        thirdCody_RV.adapter = recommend3Adapter(cody3List)
    }

    private fun sendFirstCody() {
        val selectRetrofit = Retrofit.Builder()
            .baseUrl("http://ec2-13-124-208-47.ap-northeast-2.compute.amazonaws.com:8000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val userSelectCodyRequest : selectCodyRequest = selectRetrofit.create(selectCodyRequest::class.java)

        Log.d("Start", "sendSelectFirstCody")
        Log.d(FIRST, firstCody.toString())
        val stringBuilder = StringBuilder()
        for (i in firstCody) {
            Log.d("i", i)
            stringBuilder.append(i).append(",")
            Log.d("codyList", stringBuilder.toString())
        }

        Log.d("cody", stringBuilder.toString())

        var selectCody = stringBuilder.substring(0, stringBuilder.length - 1)
        Log.d("final", selectCody)


        userSelectCodyRequest.requestSelectCody(token = userToken, userSelectCody = selectCody).enqueue(object : Callback<selectCodyResponse> {
            override fun onFailure(call: Call<selectCodyResponse>, t: Throwable) {
                Log.e("onFailure", t.message)
            }

            override fun onResponse(call: Call<selectCodyResponse>, response: Response<selectCodyResponse>) {
                if (response.isSuccessful) {
                    if (response.code() == 200) {
                        Log.d("Status 200", "Success")

                        var selectCodyResponse = response.body()

                        Log.d("onResponse", selectCodyResponse?.msg)
                    }
                }
            }

        })
    }

    private fun sendSecondCody() {
        val selectRetrofit = Retrofit.Builder()
            .baseUrl("http://ec2-13-124-208-47.ap-northeast-2.compute.amazonaws.com:8000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val userSelectCodyRequest : selectCodyRequest = selectRetrofit.create(selectCodyRequest::class.java)

        Log.d("Start", "sendSelectFirstCody")
        Log.d(SECOND, secondCody.toString())
        val stringBuilder = StringBuilder()
        for (i in secondCody) {
            Log.d("i", i)
            stringBuilder.append(i).append(",")
            Log.d("codyList", stringBuilder.toString())
        }

        Log.d("cody", stringBuilder.toString())

        var selectCody = stringBuilder.substring(0, stringBuilder.length - 1)
        Log.d("final", selectCody)


        userSelectCodyRequest.requestSelectCody(token = userToken, userSelectCody = selectCody).enqueue(object : Callback<selectCodyResponse> {
            override fun onFailure(call: Call<selectCodyResponse>, t: Throwable) {
                Log.e("onFailure", t.message)
            }

            override fun onResponse(call: Call<selectCodyResponse>, response: Response<selectCodyResponse>) {
                if (response.isSuccessful) {
                    if (response.code() == 200) {
                        Log.d("Status 200", "Success")

                        var selectCodyResponse = response.body()

                        Log.d("onResponse", selectCodyResponse?.msg)
                    }
                }
            }

        })
    }

    private fun sendThirdCody() {
        val selectRetrofit = Retrofit.Builder()
            .baseUrl("http://ec2-13-124-208-47.ap-northeast-2.compute.amazonaws.com:8000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val userSelectCodyRequest : selectCodyRequest = selectRetrofit.create(selectCodyRequest::class.java)

        Log.d("Start", "sendSelectFirstCody")
        Log.d(THIRD, thirdCody.toString())
        val stringBuilder = StringBuilder()
        for (i in thirdCody) {
            Log.d("i", i)
            stringBuilder.append(i).append(",")
            Log.d("codyList", stringBuilder.toString())
        }

        Log.d("cody", stringBuilder.toString())

        var selectCody = stringBuilder.substring(0, stringBuilder.length - 1)
        Log.d("final", selectCody)

        userSelectCodyRequest.requestSelectCody(token = userToken, userSelectCody = selectCody).enqueue(object : Callback<selectCodyResponse> {
            override fun onFailure(call: Call<selectCodyResponse>, t: Throwable) {
                Log.e("onFailure", t.message)
            }

            override fun onResponse(call: Call<selectCodyResponse>, response: Response<selectCodyResponse>) {
                if (response.isSuccessful) {
                    if (response.code() == 200) {
                        Log.d("Status 200", "Success")

                        var selectCodyResponse = response.body()

                        Log.d("onResponse", selectCodyResponse?.msg)
                    }
                }
            }

        })
    }
}
