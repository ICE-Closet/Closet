package com.example.smartice_closet.auth

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface loginRequest {
    @FormUrlEncoded
    @POST("/accounts/login/")
    fun requestLogin(
        @Field("email") s_email:String,
        @Field("password") s_pw1:String
    ) : Call<loginResponse>
}