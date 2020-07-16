package com.example.icecloset.auth

import com.example.icecloset.auth.forSignup
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface forSignupService {
    @FormUrlEncoded
    @POST("/accounts/signup/")
    fun requestSignup(
        @Field("username") s_name:String,
        @Field("email") s_email:String,
        @Field("password") s_pw1:String
    ) : Call<forSignup>
}