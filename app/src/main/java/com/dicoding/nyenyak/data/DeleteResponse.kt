package com.dicoding.nyenyak.data

import com.google.gson.annotations.SerializedName

data class DeleteResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
