package com.rzgonz.sawitpro.data.local

import android.content.Context
import com.rzgonz.sawitpro.core.SharedPreferenceService

/**
 * Created by rzgonz on 06/03/23.
 *
 */
class AppLocalDataSource(private val context: Context) {

    var isFirstShowPermission: Boolean
        set(value) = SharedPreferenceService(context).saveBoolean(
            PreferencesKey.Key_FIRST_SHOW_PERMISSION,
            value
        )
        get() =
            SharedPreferenceService(context).getBoolean(
                PreferencesKey.Key_FIRST_SHOW_PERMISSION,
                true
            )

}