package com.rzgonz.sawitpro.ui.theme

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

/**
 * Created by rzgonz on 11/03/23.
 *
 */
class MainViewModel:ViewModel() {
    val inputFoto = mutableStateOf<Uri?>(null)
}