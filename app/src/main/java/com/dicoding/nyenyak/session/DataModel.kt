package com.dicoding.nyenyak.session

data class DataModel (
    var token: String,
    val expireTime: String,
    val isLogin: Boolean = false
)