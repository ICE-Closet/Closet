package com.example.smartice_closet.selectCody

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface selectCodyRequest {
    @FormUrlEncoded
    @POST("/accounts/selectone/")
    fun requestSelectCody(
        @Header("Authorizations") token : String,
        @Field("select") userSelectCody : String
    ) : Call<selectCodyResponse>
}