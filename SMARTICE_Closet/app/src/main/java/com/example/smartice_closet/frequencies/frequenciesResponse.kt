package com.example.smartice_closet.frequencies

import com.example.smartice_closet.recommendForUser.recommendResponse
import com.google.gson.annotations.SerializedName

data class frequenciesResponse (
    @SerializedName("msg") var msg: String,
    @SerializedName("freq_url") var freqUrl: frequencyURL? = null
) {
    data class frequencyURL(
        @SerializedName("top") var topClothes: List<String>,
        @SerializedName("bottom") var bottomClothes: List<String>,
        @SerializedName("outer") var outerClothes: List<String>,
        @SerializedName("dress") var dressClothes: List<String>
    )
}