package com.example.icecloset.auth.kakao

import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.kakao.auth.ISessionCallback
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.MeV2ResponseCallback
import com.kakao.usermgmt.response.MeV2Response
import com.kakao.util.exception.KakaoException
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class SessionCallback : ISessionCallback{
    var userToken: String = ""

    override fun onSessionOpenFailed(exception: KakaoException?) {
        Log.e("Log","Session Call Back :: onSessionOpenFailed: ${exception?.message}")
    }

    override fun onSessionOpened() {
        UserManagement.getInstance().me(object : MeV2ResponseCallback() {
//            override fun onFailure(errorResult: ErrorResult?) {
//                Log.i("Log", "Session Call Back :: onFailed ${errorResult?.errorMessage}")
//            }

            override fun onSessionClosed(errorResult: ErrorResult?) {
                Log.i("Log", "Session Call Back :: onSessionClosed ${errorResult?.errorMessage}")
            }

            override fun onSuccess(result: MeV2Response?) {
                Log.i("Log", "ID : ${result!!.id}")
                Log.i("Log", "E-Mail : ${result.kakaoAccount.email}")
                Log.i("Log", "Profile Image : ${result.profileImagePath}")

                checkNotNull(result) {
                    "Session Response Null"
                }
                sendToServerKakao()
            }
        })
    }

    private fun sendToServerKakao() {
        val kakaoRetrofit = Retrofit.Builder()
            .baseUrl("http://ec2-13-124-208-47.ap-northeast-2.compute.amazonaws.com:8000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val kakaoLoginRequest : kakaoRequest = kakaoRetrofit.create(kakaoRequest::class.java)

        lateinit var result : MeV2Response

        var uid = result!!.id.toString()
        var email = result.kakaoAccount.email

        Log.d("sendToServerKakao", uid)
        Log.d("sendToServerKakao", email)

        kakaoLoginRequest.requestKakaoLogin(uid = uid, email = email).enqueue(object : Callback<kakaoResponse> {
            override fun onFailure(call: Call<kakaoResponse>, t: Throwable) {
                Log.e("onFailure", t.message)
            }

            override fun onResponse(call: Call<kakaoResponse>, response: Response<kakaoResponse>) {
                if (response.isSuccessful) {
                    var kakaoResponse =  response.body()
                    val code = kakaoResponse?.code?.let { it-> Integer.parseInt(it) }
                    if (code == 503) {
                        Log.d("onResponse", kakaoResponse?.code)
                        Log.d("onResponse", kakaoResponse?.msg)
                        Log.d("onResponse", kakaoResponse?.token)
                        userToken = kakaoResponse?.token.toString()
                    }
                    else if (code == 201) {
                        Log.d("onResponse", kakaoResponse?.code)
                        Log.d("onResponse", kakaoResponse?.msg)
                        Log.d("onResponse", kakaoResponse?.token)
                        userToken = kakaoResponse?.token.toString()
                    }
                }
                else { }
            }
        })
    }
}