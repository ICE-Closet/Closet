package com.example.smartice_closet.userCloset

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.smartice_closet.R
import com.example.smartice_closet.adapters.userClosetAdapter
import com.example.smartice_closet.models.userClosetModel
import kotlinx.android.synthetic.main.activity_view_user_closet.*
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class viewUserCloset : AppCompatActivity() {

    private var CATEGORIES = "CATEGORIES"
    private var TOKEN = "USERTOKEN"

    var userToken = ""
    var clothesCategory = ""


    var anyClothes = ArrayList<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_user_closet)

        userToken = intent.getStringExtra(TOKEN)
        Log.d(TOKEN, userToken)

        clothesCategory = intent.getStringExtra(CATEGORIES)
        Log.d(CATEGORIES, clothesCategory)

        userCategory.text = "Your ${clothesCategory} clothes"

        getUserClothes()



    }

    private fun getUserClothes() {
        val userClosetRetrofit = Retrofit.Builder()
            .baseUrl("http://ec2-13-124-208-47.ap-northeast-2.compute.amazonaws.com:8000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val myClosetRequest : userClosetRequest = userClosetRetrofit.create(userClosetRequest::class.java)

        Log.d("CHECK", userToken + clothesCategory)
        myClosetRequest.reqeustClothes(userToken = userToken, categories = clothesCategory).enqueue(object : Callback<userClosetResponse> {
            override fun onFailure(call: Call<userClosetResponse>, t: Throwable) {
                Log.e("onFailure", t.message)
                Toast.makeText(applicationContext,""+t.message,Toast.LENGTH_LONG).show()

            }

            override fun onResponse(call: Call<userClosetResponse>, response: Response<userClosetResponse>) {
                if (response.isSuccessful) {
                    if (response.code() == 200) {
                        Log.d("Status 200", "Success")


                        anyClothes = response.body()?.clothes_url as ArrayList<String>

                        Log.d("onResponse", response.body()?.msg)
                        Log.d("Clothes", anyClothes.toString())

                        var userClosetList = arrayListOf<userClosetModel>()


                        Log.d("ClothesURL", anyClothes.toString())
                        if (anyClothes != null) {
                            for (i in anyClothes.indices) {
                                userClosetList.add(userClosetModel(anyClothes[i]))
                            }
                        }

                        userCloset_rV.layoutManager = GridLayoutManager(applicationContext, 2)
                        userCloset_rV.setHasFixedSize(true)
                        userCloset_rV.adapter = userClosetAdapter(userClosetList)
                    }
                }
                else {
                    Toast.makeText(applicationContext,""+response.code(),Toast.LENGTH_LONG).show()
                }
            }

        })


    }
}