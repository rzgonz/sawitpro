package com.rzgonz.sawitpro.di


import com.google.gson.Gson
import com.rzgonz.sawitpro.core.BaseModuleProvider
import com.rzgonz.sawitpro.core.clazz
import com.rzgonz.sawitpro.network.MapsNetworkModule
import com.rzgonz.sawitpro.network.NetworkUtils
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module


class NetworkProvider private constructor() : BaseModuleProvider {

    override val modules: List<Module>
        get() = listOf(networkModule)

    private val networkModule = module {
        single { Gson() }
        single { MapsNetworkModule.provideWebClient() }
        single { NetworkUtils(context = androidContext()) }
        single { MapsNetworkModule.provideWebService(get()) }
        single { MapsNetworkModule.provideRetrofit(okHttpClient = get(), networkUtils = get()) }
    }

    companion object {

        @Volatile
        private var INSTANCE: NetworkProvider? = null

        @JvmStatic
        fun getInstance(): NetworkProvider {
            return INSTANCE ?: synchronized(clazz<NetworkProvider>()) {
                return@synchronized NetworkProvider()
            }.also {
                INSTANCE = it
            }
        }

    }
}