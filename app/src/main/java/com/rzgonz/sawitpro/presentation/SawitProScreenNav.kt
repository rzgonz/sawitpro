package com.rzgonz.sawitpro.presentation

import android.net.Uri
import com.google.gson.Gson
import com.rzgonz.sawitpro.data.dto.SawitProOcrDto


sealed class SawitProScreenNav(val route: String) {
    object Home : SawitProScreenNav("home")
    object Ocr : SawitProScreenNav("ocr")
}