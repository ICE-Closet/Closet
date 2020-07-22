package com.example.icecloset.camera


import com.example.icecloset.camera.forCamera
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface forCameraService {
    @Multipart
    @POST("/accounts/clothes_info/")
    fun requestCamera(
        @Header("Authorizations") userToken: String,
        @Part imageFile : MultipartBody.Part
    ) : Call<forCamera>
}