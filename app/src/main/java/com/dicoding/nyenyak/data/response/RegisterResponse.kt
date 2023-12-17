package com.dicoding.nyenyak.data.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

	@field:SerializedName("gender")
	val gender: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("userId")
	val userId: String? = null,

	@field:SerializedName("birthDate")
	val birthDate: String? = null,

	@field:SerializedName("age")
	val age: Int? = null,

	@field:SerializedName("status")
	val status: String? = null
)
