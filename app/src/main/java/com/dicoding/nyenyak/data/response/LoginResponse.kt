package com.dicoding.nyenyak.data.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
	@field:SerializedName("expirateTime")
	val expirateTime: String? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null,

	@field:SerializedName("token")
	val token: String? = null
)
