package com.rzgonz.sawitpro.network

import com.rzgonz.sawitpro.data.remote.response.DistanceResponse
import retrofit2.http.GET
import retrofit2.http.QueryMap


/**
 * Created by rzgonz on 8/2/17.
 */

interface MapsApiService {

    @GET("maps/api/distancematrix/json")
    suspend fun getDistanceInfo(
        @QueryMap parameters: Map<String, String>
    ): DistanceResponse

}