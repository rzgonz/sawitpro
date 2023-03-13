package com.rzgonz.sawitpro.data.remote.response

import com.google.gson.annotations.SerializedName

data class DistanceMatrixResponse(

	@field:SerializedName("originAddresses")
	val originAddresses: List<String?>? = null,

	@field:SerializedName("destinationAddresses")
	val destinationAddresses: List<String?>? = null,

	@field:SerializedName("rows")
	val rows: List<RowsItemResponse?>? = null
)