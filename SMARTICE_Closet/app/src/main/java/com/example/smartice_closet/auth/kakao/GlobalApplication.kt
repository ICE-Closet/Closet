package com.example.smartice_closet.auth.kakao

import android.app.Application
import com.kakao.auth.KakaoSDK

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        instance = this
        KakaoSDK.init(KakaoSDKAdapter())
    }

    override fun onTerminate() {
        super.onTerminate()
        instance = null
    }

    fun getGlobalApplicationContext() : GlobalApplication {
        checkNotNull(instance) {
            "This Application does not inherit com.kakao.GlobalApplication"
        }
        return instance!!
    }

    companion object {
        var instance : GlobalApplication? = null
    }
}