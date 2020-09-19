package com.example.smartice_closet.recommendForUser

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import com.example.smartice_closet.R
import com.example.smartice_closet.adapters.recommend1Adapter
import com.example.smartice_closet.models.recommend1
import kotlinx.android.synthetic.main.activity_recommend.*
import kotlinx.android.synthetic.main.fragment_closet.*

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

        firstCody_RV.layoutManager = LinearLayoutManager(this, OrientationHelper.HORIZONTAL, false)
        firstCody_RV.setHasFixedSize(true)

        firstCody_RV.adapter = recommend1Adapter(cody1List)
    }
}
