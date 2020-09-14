package com.example.smartice_closet.homeDialog

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface homeDialogRequest {
    @FormUrlEncoded
    @POST("/accounts/google_login/")
    fun requestHomeDialog(
        @Header("Authorizations") userToken:String,
        @Field("sex") userGender:String
    ) : Call<homeDialogResponse>
}