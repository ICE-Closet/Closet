package com.example.smartice_closet.auth

data class signupResponse (
    val code: String,   // 201 : Success & 400 : 등록된 Email
    val msg: String
)