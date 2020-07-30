package com.example.icecloset.auth.google

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface forGoogleLoginService {
    @FormUrlEncoded
    @POST("/accounts/google_login/")
    fun requestGoogleLogin(
        @Field("uid") uid:String,
        @Field("email") email:String
    ) : Call<forGoogleLogin>
}