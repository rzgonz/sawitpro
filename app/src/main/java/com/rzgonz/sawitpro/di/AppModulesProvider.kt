package com.rzgonz.sawitpro.di


import com.rzgonz.sawitpro.core.clazz
import org.koin.core.module.Module

class AppModulesProvider private constructor() {

    val appModules: List<Module>
        get() {
            return ArrayList<Module>().apply {
                addAll(appProvider)
                addAll(networkModules)
            }
        }


    private val appProvider by lazy {
        AppProvider.getInstance().modules
    }
    private val networkModules by lazy {
        NetworkProvider.getInstance().modules
    }


    companion object {

        @Volatile
        private var INSTANCE: AppModulesProvider? = null

        @JvmStatic
        fun getInstance(
        ): AppModulesProvider {
            return INSTANCE ?: synchronized(clazz<AppModulesProvider>()) {
                return@synchronized AppModulesProvider()
            }.also {
                INSTANCE = it
            }
        }

    }
}