package com.dicoding.nyenyak.data.response

import com.google.gson.annotations.SerializedName

data class GetDetailDiagnosisResponse(

	@field:SerializedName("bloodPressure")
	val bloodPressure: String? = null,

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("qualityOfSleep")
	val qualityOfSleep: Int? = null,

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

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("sleepDuration")
	val sleepDuration: Any? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("age")
	val age: Int? = null
)
