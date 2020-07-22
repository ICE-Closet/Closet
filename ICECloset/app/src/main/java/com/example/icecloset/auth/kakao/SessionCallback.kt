package com.example.icecloset.auth.kakao

import android.util.Log
import com.kakao.auth.ISessionCallback
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.MeV2ResponseCallback
import com.kakao.usermgmt.response.MeV2Response
import com.kakao.util.exception.KakaoException

class SessionCallback : ISessionCallback{
    override fun onSessionOpenFailed(exception: KakaoException?) {
        Log.e("Log","Session Call Back :: onSessionOpenFailed: ${exception?.message}")
    }

    override fun onSessionOpened() {
        UserManagement.getInstance().me(object : MeV2ResponseCallback() {
            override fun onFailure(errorResult: ErrorResult?) {
                Log.i("Log", "Session Call Back :: onFailed ${errorResult?.errorMessage}")
            }

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
            }
        })
    }
}