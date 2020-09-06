package com.example.smartice_closet.auth.google

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface googleRequest {
    @FormUrlEncoded
    @POST("/accounts/google_login/")
    fun requestGoogleLogin(
        @Field("uid") uid:String,
        @Field("email") email:String
    ) : Call<googleResponse>
}