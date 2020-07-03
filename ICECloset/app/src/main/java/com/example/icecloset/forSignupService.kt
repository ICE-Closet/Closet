package com.example.icecloset

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface forSignupService {
    @FormUrlEncoded
    @POST("")
    fun requestSignup(
        @Field("username") s_name:String,
        @Field("email") s_email:String,
        @Field("password1") s_pw1:String,
        @Field("password2") s_pw2:String
    ) : Call<forSignup>
}