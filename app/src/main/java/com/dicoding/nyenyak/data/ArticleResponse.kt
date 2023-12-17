package com.dicoding.nyenyak.data

import com.google.gson.annotations.SerializedName

data class ArticleResponse(

	@field:SerializedName("ArticleResponse")
	val articleResponse: List<ArticleResponseItem?>? = null
)

data class ArticleResponseItem(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("content")
	val content: String? = null
)
