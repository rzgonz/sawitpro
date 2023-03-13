package com.rzgonz.sawitpro.data.remote.response

import com.google.gson.annotations.SerializedName

data class DurationResponse(

	@field:SerializedName("text")
	val text: String? = null,

	@field:SerializedName("value")
	val value: Int? = null
)