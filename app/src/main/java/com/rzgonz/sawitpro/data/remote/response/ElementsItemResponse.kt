package com.rzgonz.sawitpro.data.remote.response

import com.google.gson.annotations.SerializedName

data class ElementsItemResponse(

    @field:SerializedName("duration")
	val duration: DurationResponse? = null,

    @field:SerializedName("distance")
	val distanceResponse: DistanceResponse? = null,

    @field:SerializedName("status")
	val status: String? = null
)