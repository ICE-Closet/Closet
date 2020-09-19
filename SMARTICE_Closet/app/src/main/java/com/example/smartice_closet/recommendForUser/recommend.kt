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
import kotlinx.android.synthetic.main.activity_recommend.*


class recommend : AppCompatActivity() {

    private val TOKEN = "USERTOKEN"

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recommend)

        var userToken = intent.getStringExtra(TOKEN)
        Log.d(TOKEN, userToken)

        val cody1List = arrayListOf<recommend1>(
            recommend1("http://13.124.208.47:8000/media/2020/09/02/15.jpg"),
            recommend1("http://13.124.208.47:8000/media/2020/09/02/15.jpg"),
            recommend1("http://13.124.208.47:8000/media/2020/09/02/15.jpg")
        )

        val cody2List = arrayListOf<recommend2>(
            recommend2("http://13.124.208.47:8000/media/2020/09/02/15.jpg"),
            recommend2("http://13.124.208.47:8000/media/2020/09/02/15.jpg"),
            recommend2("http://13.124.208.47:8000/media/2020/09/02/15.jpg")
        )

        val cody3List = arrayListOf<recommend3>(
            recommend3("http://13.124.208.47:8000/media/2020/09/02/15.jpg"),
            recommend3("http://13.124.208.47:8000/media/2020/09/02/15.jpg"),
            recommend3("http://13.124.208.47:8000/media/2020/09/02/15.jpg")
        )

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
}
