package com.rzgonz.sawitpro.data.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SawitProOcrDto(
    val text: String = "",
    val distance: String = "",
    val duration: String = ""
) : Parcelable