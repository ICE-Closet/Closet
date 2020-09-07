package com.example.smartice_closet.fragments

import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.smartice_closet.R
import com.example.smartice_closet.profileGET.profileGETRequest
import com.example.smartice_closet.profileGET.profileGETResponse
import com.example.smartice_closet.profilePOST.profilePOSTRequest
import com.example.smartice_closet.profilePOST.profilePOSTResponse
import kotlinx.android.synthetic.main.fragment_profile.*
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

/**
 * A simple [Fragment] subclass.
 * Use the [profileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class profileFragment : Fragment() {

    private val USERNAME = "USERNAME"
    private val TOKEN = "USERTOKEN"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = arguments
        val userName = bundle!!.getString(USERNAME)
        val userToken = bundle!!.getString(TOKEN)
        Log.d("onViewCreated", userName + "\n" + userToken)
        
        getFromServer(userToken)

        setUserName(userName)

        update_btn.setOnClickListener {
            sendToServer(userToken)

        }
    }

    private fun setUserName(userName: String?) {
        user_Name.text = "${userName}".toUpperCase()
        small_user_Name.text = "${userName}".toLowerCase()
        profile_Name_eT.text = Editable.Factory.getInstance().newEditable(userName)
    }

    private fun getFromServer(userToken: String?) {
        val BaseURL = "http://ec2-13-124-208-47.ap-northeast-2.compute.amazonaws.com:8000"

        val retrofit = Retrofit.Builder()
            .baseUrl(BaseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val profileGETService = retrofit.create(profileGETRequest::class.java)
        Log.d("Check", userToken)

        if (userToken != null) {
            profileGETService.getUserInfo(token = userToken).enqueue(object : Callback<profileGETResponse> {
                override fun onFailure(call: Call<profileGETResponse>, t: Throwable) {
                    Log.d("onFailure(ProfileGET)", "message : " + t.message)
                }

                override fun onResponse(call: Call<profileGETResponse>, response: Response<profileGETResponse>) {
                    if (response.isSuccessful) {
                        if (response.code() == 200) {
                            val profileResponse = response.body()
                            Log.d("onResponse(getProfile)", "messsage : " + profileResponse?.email)
                            Log.d("onResponse(getProfile)", "messsage : " + profileResponse?.raspIp)
                            Log.d("onResponse(getProfile)", "messsage : " + profileResponse?.raspPort)

                            var email = profileResponse?.email
                            var raspIp = profileResponse?.raspIp
                            var raspPort = profileResponse?.raspPort

                            profile_Email_eT.text = Editable.Factory.getInstance().newEditable(email)
                            profile_IP_eT.text = Editable.Factory.getInstance().newEditable(raspIp)
                            profile_PORT_eT.text = Editable.Factory.getInstance().newEditable(raspPort)
                        }
                    }
                }
            })
        }
    }

    private fun sendToServer(userToken: String?) {
        val BaseURL = "http://ec2-13-124-208-47.ap-northeast-2.compute.amazonaws.com:8000"
        var userIP = profile_IP_eT.text.toString()
        var userPORT = profile_PORT_eT.text.toString()

        val retrofit = Retrofit.Builder()
            .baseUrl(BaseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val profilePOSTService = retrofit.create(profilePOSTRequest::class.java)

        if (userToken != null) {
            profilePOSTService.sendUserInfo(token = userToken, raspIp = userIP, raspPORT = userPORT)
                .enqueue(object : Callback<profilePOSTResponse> {
                    override fun onFailure(call: Call<profilePOSTResponse>, t: Throwable) {
                        Log.d("onFailure(ProfilePOST)", "message : " + t.message)
                    }

                    override fun onResponse(call: Call<profilePOSTResponse>, response: Response<profilePOSTResponse>) {
                        if (response.isSuccessful) {
                            if (response.code() == 201) {
                                val msg = response.body()?.msg
                                Log.d("onResponse - code 201", msg)
                                Toast.makeText(context, "정보 저장이 완료 되었습니다.", Toast.LENGTH_SHORT).show()
                            }
                            else if (response.code() == 200) {
                                val msg = response.body()?.msg
                                Log.d("onResponse - code 200", msg)
                                Toast.makeText(context, "정보가 수정되었습니다.", Toast.LENGTH_SHORT).show()
                            }
                        }
                        else {
                            if (response.code() == 400) {
                                Toast.makeText(context, "정보 저장에 실패 하였습니다.\n잠시후 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                })
        }
    }






}