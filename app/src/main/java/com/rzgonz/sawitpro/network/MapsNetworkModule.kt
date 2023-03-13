package com.rzgonz.sawitpro.network

import com.rzgonz.sawitpro.BuildConfig
import com.rzgonz.sawitpro.core.clazz
import com.rzgonz.sawitpro.core.logI
import com.rzgonz.sawitpro.network.MapsApiService
import com.rzgonz.sawitpro.network.NetworkUtils
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by rzgonz on 02/02/23.
 *
 */

object MapsNetworkModule {

    private const val requestTimeOut = 120L

    private val builder by lazy {
        logI<MapsNetworkModule>("Init network")
        OkHttpClient.Builder()
    }


    fun provideWebService(retrofit: Retrofit): MapsApiService =
        retrofit.create(clazz<MapsApiService>())


    fun provideRetrofit(okHttpClient: OkHttpClient, networkUtils: NetworkUtils): Retrofit =
        Retrofit.Builder()
            .baseUrl(networkUtils.getMapsBaseUrl())
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()


    fun provideWebClient(): OkHttpClient {
        val logInterceptor = HttpLoggingInterceptor().apply {
            logI<MapsNetworkModule>("is debug")
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
            else HttpLoggingInterceptor.Level.NONE
        }
        val httpClient = builder.apply {
            addNetworkInterceptor(logInterceptor)
            callTimeout(requestTimeOut, TimeUnit.SECONDS)
            connectTimeout(requestTimeOut, TimeUnit.SECONDS)
            readTimeout(requestTimeOut, TimeUnit.SECONDS)
            writeTimeout(requestTimeOut, TimeUnit.SECONDS)
        }

        return httpClient.build()
    }


}