package com.rzgonz.sawitpro


import android.app.Application
import com.rzgonz.sawitpro.di.AppModulesProvider
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.module.Module

/**
 * Created by rzgonz on 7/12/17.
 */

class AppApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        setupKoin()

    }


    private fun setupKoin() {
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@AppApplication)
            modules(mutableListOf<Module>().apply {
                addAll(
                    AppModulesProvider.getInstance().appModules
                )
            })
        }
    }

}
