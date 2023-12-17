package com.dicoding.nyenyak.data

import com.google.gson.annotations.SerializedName

data class InputResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("newDiagnosis")
	val newDiagnosis: NewDiagnosis? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class NewDiagnosis(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("qualityOfSleep")
	val qualityOfSleep: Int? = null,

	@field:SerializedName("bloodPressure")
	val bloodPressure: String? = null,

	@field:SerializedName("gender")
	val gender: String? = null,

	@field:SerializedName("sleepDisorder")
	val sleepDisorder: String? = null,

	@field:SerializedName("stressLevel")
	val stressLevel: Int? = null,

	@field:SerializedName("BMIcategory")
	val bMIcategory: String? = null,

	@field:SerializedName("physicalActivityLevel")
	val physicalActivityLevel: Int? = null,

	@field:SerializedName("uid")
	val uid: String? = null,

	@field:SerializedName("dailySteps")
	val dailySteps: Int? = null,

	@field:SerializedName("heartRate")
	val heartRate: Int? = null,

	@field:SerializedName("solution")
	val solution: String? = null,

	@field:SerializedName("sleepDuration")
	val sleepDuration: Any? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("age")
	val age: Int? = null
)
