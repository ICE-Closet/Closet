package com.example.smartice_closet.profileGET

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface profileGETRequest {
    @GET("/accounts/mypage/")
    fun getUserInfo(
        @Header("token") token: String
    ) : Call<profileGETResponse>
}