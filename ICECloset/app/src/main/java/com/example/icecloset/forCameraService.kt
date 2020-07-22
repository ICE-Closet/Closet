package com.example.icecloset


import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface forCameraService {
    @Multipart
    @POST("/clothes_info/")
    fun requestCamera(
        @Part imageFile : MultipartBody.Part
    ) : Call<forCamera>
}