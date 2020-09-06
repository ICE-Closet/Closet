package com.example.smartice_closet.auth

data class loginResponse (
    val code : String,  // 201
    val msg : String,
    val token :String
)