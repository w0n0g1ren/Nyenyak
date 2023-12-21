package com.dicoding.nyenyak.data.response

import com.google.gson.annotations.SerializedName

data class GetDetailUserResponse(

	@field:SerializedName("user")
	val user: User? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class User(

	@field:SerializedName("gender")
	val gender: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("birthDate")
	val birthDate: String? = null,

	@field:SerializedName("age")
	val age: Int? = null,

	@field:SerializedName("email")
	val email: String? = null
)
