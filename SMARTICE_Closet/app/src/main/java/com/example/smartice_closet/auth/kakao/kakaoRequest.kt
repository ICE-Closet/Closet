package com.example.smartice_closet.auth.kakao

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface kakaoRequest {
    @FormUrlEncoded
    @POST("/accounts/kakao_login/")
    fun requestKakaoLogin(
        @Field("uid") uid: String,
        @Field("email") email: String,
        @Field("sex") gender: String
    ) : Call<kakaoResponse>
}