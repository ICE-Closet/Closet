package com.example.smartice_closet.auth

data class loginResponse (
    val code : String,
    val msg : String,
    val token : String,
    val name : String
)