package com.example.smartice_closet.profilePOST

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface profilePOSTRequest {
    @FormUrlEncoded
    @POST("/accounts/mypage/")
    fun sendUserInfo(
        @Header("Authorizations") token : String,
        @Field("rasp_ip") raspIp : String,
        @Field("rasp_port") raspPORT : String
    ) : Call<profilePOSTResponse>
}