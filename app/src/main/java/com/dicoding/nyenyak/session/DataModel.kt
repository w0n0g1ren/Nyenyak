package com.dicoding.nyenyak.session

data class DataModel (
    var token: String,
    val name: String,
    val userId: String,
    val isLogin: Boolean = false
)