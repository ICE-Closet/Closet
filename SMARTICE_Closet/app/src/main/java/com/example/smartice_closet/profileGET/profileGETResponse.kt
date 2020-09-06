package com.example.smartice_closet.profileGET

import com.google.gson.annotations.SerializedName

data class profileGETResponse (
    @SerializedName("code") var code: Int = 0,
    @SerializedName("email") var email: String? = null,
    @SerializedName("rasp_ip") var raspIp: String? = null,
    @SerializedName("rasp_port") var raspPort: String? = null
)