package com.rzgonz.sawitpro.domain

import com.rzgonz.sawitpro.data.local.AppRepository

/**
 * Created by rzgonz on 12/03/23.
 *
 */
class AppUseCaseImpl(
    private val appRepository: AppRepository
) : AppUseCase {
    override fun isFirstShowPermission(): Boolean {
        return appRepository.isFirstShowPermission
    }

    override fun disableFirstShowPermission() {
        appRepository.isFirstShowPermission = false
    }

}