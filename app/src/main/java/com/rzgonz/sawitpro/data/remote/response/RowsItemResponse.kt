package com.rzgonz.sawitpro.data.remote.response

import com.google.gson.annotations.SerializedName

data class RowsItemResponse(

	@field:SerializedName("elements")
	val elements: List<ElementsItemResponse?>? = null
)