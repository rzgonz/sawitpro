package com.rzgonz.sawitpro.data.remote

import com.rzgonz.sawitpro.network.MapsApiService

/**
 * Created by rzgonz on 13/03/23.
 *
 */
class MapRemoteDataSource(
    private val mapsApiService: MapsApiService
) : MapsApiService by mapsApiService