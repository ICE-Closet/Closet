package com.example.smartice_closet.weather

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface weatherRequest {
    @GET("data/2.5/weather")
    fun getCurrentWeatherData(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("appid") appid: String
    ) : Call<weatherResponse>
}