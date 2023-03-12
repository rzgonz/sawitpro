package com.rzgonz.sawitpro.data.local

/**
 * Created by rzgonz on 12/03/23.
 *
 */
class AppRepository(
    private val appLocalDataSource: AppLocalDataSource
) {

    var isFirstShowPermission: Boolean
        set(value) {
            appLocalDataSource.isFirstShowPermission = value
        }
        get() = appLocalDataSource.isFirstShowPermission
}