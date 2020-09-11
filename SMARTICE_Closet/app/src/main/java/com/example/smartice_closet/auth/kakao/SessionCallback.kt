package com.example.smartice_closet.auth.kakao

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.smartice_closet.auth.login
import com.example.smartice_closet.main
import com.kakao.auth.ISessionCallback
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.MeV2ResponseCallback
import com.kakao.usermgmt.response.MeV2Response
import com.kakao.util.exception.KakaoException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SessionCallback (val context: login): ISessionCallback {

    var userToken: String = ""

    private val USERNAME = "USERNAME"
    private val TOKEN = "USERTOKEN"
    private val USERGENDER = "USERGENDER"

    override fun onSessionOpenFailed(exception: KakaoException?) {
        Log.e("Log","Session Call Back :: onSessionOpenFailed: ${exception?.message}")
    }

    override fun onSessionOpened() {
        UserManagement.getInstance().me(object : MeV2ResponseCallback() {
            override fun onSuccess(result: MeV2Response?) {
                Log.i("Log", "ID : ${result!!.id}")
                Log.i("Log", "E-Mail : ${result.kakaoAccount.email}")
                Log.i("Log", "Gender : ${result.kakaoAccount.gender}")

                checkNotNull(result) {
                    "Session Response Null"
                }

                sendToServerKakao(result)
            }

            override fun onSessionClosed(errorResult: ErrorResult?) {
                Log.i("Log", "Session Call Back :: onSessionClosed ${errorResult?.errorMessage}")
            }

            override fun onFailure(errorResult: ErrorResult?) {
                Log.i("Log", "Session Call back :: on failed ${errorResult?.errorMessage}")
            }
        })
    }

    private fun sendToServerKakao(result: MeV2Response) {
        val kakaoRetrofit = Retrofit.Builder()
            .baseUrl("http://ec2-13-124-208-47.ap-northeast-2.compute.amazonaws.com:8000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val kakaoLoginRequest : kakaoRequest = kakaoRetrofit.create(kakaoRequest::class.java)

        var uid = result.id.toString()
        var email = result.kakaoAccount.email.toString()
        var gender = result.kakaoAccount.gender.toString()

        Log.d("sendToServerKakao", uid)
        Log.d("sendToServerKakao", email)
        Log.d("sendToServerKakao", gender)

        kakaoLoginRequest.requestKakaoLogin(uid = uid, email = email, gender = gender).enqueue(object : Callback<kakaoResponse> {
            override fun onFailure(call: Call<kakaoResponse>, t: Throwable) {
                Log.e("onFailure", t.message)
            }

            override fun onResponse(call: Call<kakaoResponse>, response: Response<kakaoResponse>) {
                if (response.isSuccessful) {
                    if (response.code() == 200) {
                        Log.d("Status 200", "Success")
                        var kakao_response = response.body()

                        Log.d("onResponse", kakao_response?.msg)
                        Log.d("onResponse", kakao_response?.name)
                        Log.d("onResponse", kakao_response?.token)

                        userToken = kakao_response?.token.toString()
                        var name = kakao_response?.name.toString()
                        var gender = kakao_response?.sex.toString()

                        var intent = Intent(context, main::class.java).apply {
                            putExtra(TOKEN, userToken)
                            putExtra(USERNAME, name)
                            putExtra(USERGENDER, gender)
                        }
                        context.startActivity(intent)
                        (context as Activity).finish()
                    }
                }
                else {
                    if (response.code() == 400) {
                        Toast.makeText(context, "잠시 후 다시 로그인 해주세요.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}