package com.rzgonz.sawitpro.di


import com.rzgonz.sawitpro.presentation.main.MainViewModel
import com.rzgonz.sawitpro.core.BaseModuleProvider
import com.rzgonz.sawitpro.core.clazz
import com.rzgonz.sawitpro.data.local.AppLocalDataSource
import com.rzgonz.sawitpro.data.AppRepository
import com.rzgonz.sawitpro.data.remote.MapRemoteDataSource
import com.rzgonz.sawitpro.domain.AppUseCase
import com.rzgonz.sawitpro.domain.AppUseCaseImpl

import com.rzgonz.sawitpro.presentation.ocr.OcrViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.binds
import org.koin.dsl.module

class AppProvider private constructor() : BaseModuleProvider {

    override val modules: List<Module>
        get() = listOf(appsModule, interactorModule, viewModelModule)

    private val appsModule = module {

        single {
            AppLocalDataSource(context = get())
        }

        single {
            MapRemoteDataSource(mapsApiService = get())
        }

        single {
            AppRepository(
                appLocalDataSource = get(),
                mapsRemoteDataSource = get()
            )
        }

    }


    private val viewModelModule = module {
        viewModel { MainViewModel(appUseCase = get()) }
        viewModel { OcrViewModel(appUseCase = get()) }
    }

    private val interactorModule = module {
        factory {
            AppUseCaseImpl(
                appRepository = get()
            )
        } binds arrayOf(AppUseCase::class)
    }


    companion object {

        @Volatile
        private var INSTANCE: AppProvider? = null

        @JvmStatic
        fun getInstance(): AppProvider {
            return INSTANCE ?: synchronized(clazz<AppProvider>()) {
                return@synchronized AppProvider()
            }.also {
                INSTANCE = it
            }
        }

    }


}