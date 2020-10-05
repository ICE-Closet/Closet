package com.example.smartice_closet.userCloset

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface userClosetRequest {
    @FormUrlEncoded
    @POST("/accounts/myclothes/")
    fun reqeustClothes(
        @Header("Authorizations") userToken : String,
        @Field("category") categories : String
    ) : Call<userClosetResponse>
}