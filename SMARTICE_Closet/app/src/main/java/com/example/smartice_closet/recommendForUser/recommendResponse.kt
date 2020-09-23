package com.example.smartice_closet.recommendForUser

import com.google.gson.annotations.SerializedName

data class recommendResponse (
    @SerializedName("msg") var msg: String,
    @SerializedName("media_url") var mediaUrl: MediaURL? = null
) {
    data class MediaURL(
        @SerializedName("first") var firstCody: List<String>,
        @SerializedName("second") var secondCody: List<String>,
        @SerializedName("third") var thirdCody: List<String>
    )
}