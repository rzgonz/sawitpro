package com.rzgonz.sawitpro.network

import android.content.Context


class NetworkUtils(private val context: Context) {

    private var mapsBaseUrl = "https://maps.googleapis.com/maps/api/"

    fun setMapsBaseUrl(baseUrl: String) {
        this.mapsBaseUrl = baseUrl
    }

    fun getMapsBaseUrl(): String = mapsBaseUrl

}
