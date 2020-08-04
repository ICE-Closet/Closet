package com.example.icecloset.camera

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface forCameraService {
    @FormUrlEncoded
//    @Multipart
    @POST("/accounts/clothes_info/")
    fun requestCamera(
        @Header("Authorizations") userToken: String,
        @Part imageFile : MultipartBody.Part
//        @Part("top") top: String,
//        @Part("bottom") bottom: String,
//        @Part("outer") outer: String
//        @Field("color") color: String
    ) : Call<forCamera>
}