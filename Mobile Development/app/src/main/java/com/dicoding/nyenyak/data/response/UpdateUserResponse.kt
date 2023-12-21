package com.dicoding.nyenyak.data.response

import com.google.gson.annotations.SerializedName

data class UpdateUserResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class Data(

	@field:SerializedName("gender")
	val gender: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("birthDate")
	val birthDate: String? = null,

	@field:SerializedName("age")
	val age: Int? = null
)
