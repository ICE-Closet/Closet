package com.example.smartice_closet.frequencies

import retrofit2.Call
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header

interface frequenciesRequest {
    @GET("/accounts/frequency/")
    fun requestFrequency(
        @Header("Authorizations") token : String
    ) : Call<frequenciesResponse>
}