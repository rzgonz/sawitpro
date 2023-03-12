package com.rzgonz.sawitpro

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.rzgonz.sawitpro.core.orFalse
import com.rzgonz.sawitpro.domain.AppUseCase

/**
 * Created by rzgonz on 11/03/23.
 *
 */
class MainViewModel(
    private val appUseCase: AppUseCase
) : ViewModel() {
    val inputFoto = mutableStateOf<Uri?>(null)
    val ocrText = mutableStateOf<String>("")
    val lastLat = mutableStateOf<Double>(0.0)
    val lastLong = mutableStateOf<Double>(0.0)
    val permissionGranted = mutableStateOf(false)
    val isGpsOn = mutableStateOf(false)
    val isFirstShowPermission = mutableStateOf<Boolean>(appUseCase.isFirstShowPermission())

    fun hiddenFirstShowPermission() {
        appUseCase.disableFirstShowPermission()
        isFirstShowPermission.value.orFalse()
    }
}