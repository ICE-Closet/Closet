package com.example.smartice_closet.recommendForUser

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface recommendRequest {
    @FormUrlEncoded
    @POST("/accounts/recommendation/")
    fun requestRecommend(
        @Header("Authorizations") token : String,
        @Field("style") userStyle : String,
        @Field("color") userColor : String,
        @Field("weather") weather : String
    ) : Call<recommendResponse>
}