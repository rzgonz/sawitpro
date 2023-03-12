package com.rzgonz.sawitpro.domain

/**
 * Created by rzgonz on 12/03/23.
 *
 */
interface AppUseCase {
    fun isFirstShowPermission(): Boolean
    fun disableFirstShowPermission()
}