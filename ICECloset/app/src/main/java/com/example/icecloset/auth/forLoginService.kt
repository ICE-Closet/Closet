package com.example.icecloset.auth

import com.example.icecloset.auth.forLogin
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface forLoginService {
    @FormUrlEncoded
    @POST("/accounts/login/")
    fun requestLogin(
        @Field("email") s_email:String,
        @Field("password") s_pw1:String
    ) : Call<forLogin>
}