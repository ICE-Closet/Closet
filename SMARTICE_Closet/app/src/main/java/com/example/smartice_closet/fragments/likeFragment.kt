package com.example.smartice_closet.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import com.example.smartice_closet.R
import com.example.smartice_closet.adapters.BottomAdapter
import com.example.smartice_closet.adapters.DressAdapter
import com.example.smartice_closet.adapters.OuterAdapter
import com.example.smartice_closet.adapters.TopAdapter
import com.example.smartice_closet.frequencies.frequenciesRequest
import com.example.smartice_closet.frequencies.frequenciesResponse
import com.example.smartice_closet.models.bottomFrequencies
import com.example.smartice_closet.models.dressFrequencies
import com.example.smartice_closet.models.outerFrequencies
import com.example.smartice_closet.models.topFrequencies
import kotlinx.android.synthetic.main.fragment_like.*
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory


/**
 * A simple [Fragment] subclass.
 * Use the [likeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class likeFragment : Fragment() {
    private val TOKEN = "USERTOKEN"
    private val USERGENDER = "USERGENDER"

    var userToken = ""
    var userGender = ""

    var topList = ArrayList<String>()
    var bottomList = ArrayList<String>()
    var outerList = ArrayList<String>()
    var dressList = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_like, container, false)
    }


    @SuppressLint("WrongConstant")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = arguments
        userToken = bundle!!.getString(TOKEN).toString()
        userGender = bundle!!.getString(USERGENDER).toString()


//        val topWear: ArrayList<String> = ArrayList()  // 나중에 String을 이미지로 바꿔야 함
//        val bottomWear: ArrayList<String> = ArrayList()
//        val outerWear: ArrayList<String> = ArrayList()
//        val dressWear: ArrayList<String> = ArrayList()

        getClothesFrequencies()


    }

    private fun getClothesFrequencies() {
        val frequencyRetrofit = Retrofit.Builder()
            .baseUrl("http://ec2-13-124-208-47.ap-northeast-2.compute.amazonaws.com:8000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val userFrequencyRequest : frequenciesRequest = frequencyRetrofit.create(frequenciesRequest::class.java)

        userFrequencyRequest.requestFrequency(token = userToken).enqueue(object : Callback<frequenciesResponse> {
            override fun onFailure(call: Call<frequenciesResponse>, t: Throwable) {
                Log.e("onFailure", t.message)
            }

            @SuppressLint("WrongConstant")
            override fun onResponse(call: Call<frequenciesResponse>, response: Response<frequenciesResponse>) {
                if (response.isSuccessful) {
                    if (response.code() == 200) {
                        Log.d("Status 200", "Success")

                        var frequencyResponse = response.body()

                        Log.d("onResponse : msg", frequencyResponse?.msg)
                        Log.d("onResponse : freq_url", frequencyResponse?.freqUrl.toString())

                        topList = frequencyResponse?.freqUrl?.topClothes as ArrayList<String>
                        bottomList = frequencyResponse?.freqUrl?.bottomClothes as ArrayList<String>
                        outerList = frequencyResponse?.freqUrl?.outerClothes as ArrayList<String>
                        dressList = frequencyResponse?.freqUrl?.dressClothes as ArrayList<String>
                        Log.d("sex", bottomList.toString())

                        Log.d("topURL", topList.toString())
                        Log.d("bottomURL", bottomList.toString())
                        Log.d("outerURL", outerList.toString())
                        Log.d("dressURL", dressList.toString())

                        var topClosetList = arrayListOf<topFrequencies>()
                        var bottomClosetList = arrayListOf<bottomFrequencies>()
                        var outerClosetList = arrayListOf<outerFrequencies>()
                        var dressClosetList = arrayListOf<dressFrequencies>()

                        if (topList != null) {
                            for (i in topList.indices) {
                                topClosetList.add(topFrequencies(topList[i]))
                            }
                        }

                        if (bottomList != null) {
                            for (i in bottomList.indices) {
                                bottomClosetList.add(bottomFrequencies(bottomList[i]))
                                Log.d("sss", bottomClosetList.toString())
                            }
                        }

                        if (outerList != null) {
                            for (i in outerList.indices) {
                                outerClosetList.add(outerFrequencies(outerList[i]))
                            }
                        }

                        if (dressList != null) {
                            for (i in dressList.indices) {
                                dressClosetList.add(dressFrequencies(dressList[i]))
                            }
                        }

                        topRecyclerView.layoutManager = LinearLayoutManager(context, OrientationHelper.HORIZONTAL, false)
                        topRecyclerView.adapter = TopAdapter(topClosetList)

                        bottomRecyclerView.layoutManager = LinearLayoutManager(context, OrientationHelper.HORIZONTAL, false)
                        bottomRecyclerView.adapter = BottomAdapter(bottomClosetList)

                        outerRecyclerView.layoutManager = LinearLayoutManager(context, OrientationHelper.HORIZONTAL, false)
                        outerRecyclerView.adapter = OuterAdapter(outerClosetList)

                        dressRecyclerView.layoutManager = LinearLayoutManager(context, OrientationHelper.HORIZONTAL, false)
                        dressRecyclerView.adapter = DressAdapter(dressClosetList)

                    }
                }
                else{
                    Toast.makeText(context, ""+response.code(), Toast.LENGTH_LONG).show()
                }
            }

        })
    }
}