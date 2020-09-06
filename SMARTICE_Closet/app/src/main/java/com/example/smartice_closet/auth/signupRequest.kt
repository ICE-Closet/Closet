package com.example.smartice_closet.auth

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface signupRequest {
    @FormUrlEncoded
    @POST("/accounts/signup/")
    fun requestSignup(
        @Field("username") s_name:String,
        @Field("email") s_email:String,
        @Field("password") s_pw1:String,
        @Field("sex") s_gender:String
    ) : Call<signupResponse>
}