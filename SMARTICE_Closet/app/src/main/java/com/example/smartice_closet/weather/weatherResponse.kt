package com.example.smartice_closet.weather

import com.google.gson.annotations.SerializedName

data class weatherResponse(
    @SerializedName("cod") var code: Int = 0,
    @SerializedName("main") var main: Main? = null,
    @SerializedName("sys") var sys: Sys? = null
) {
    data class Main(
        @SerializedName("temp") var temp: Float = 0.toFloat(),
        @SerializedName("temp_max") var temp_max: Float = 0.toFloat(),
        @SerializedName("temp_min") var temp_min: Float = 0.toFloat()
    )

    data class Sys(
        @SerializedName("country") var country: String? = null
    )
}