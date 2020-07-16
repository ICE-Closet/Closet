package com.example.icecloset.auth

data class forSignup (
    val code: String,   // 201 : Success & 400 : 등록된 Email
    val msg: String
)