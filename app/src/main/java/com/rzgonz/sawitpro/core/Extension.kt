package com.rzgonz.sawitpro.core

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.widget.Toast


inline fun <reified T : Any> logD(s: String) = Log.d(tag<T>(), s)

inline fun <reified T : Any> logE(s: String) = Log.e(tag<T>(), s)

inline fun <reified T : Any> logI(s: String) = Log.i(tag<T>(), s)

inline fun <reified T : Any> logV(s: String) = Log.v(tag<T>(), s)

inline fun <reified T : Any> logW(s: String) = Log.w(tag<T>(), s)

inline fun <reified T : Any> clazz() = T::class.java

inline fun <reified T : Any> tag() = T::class.java.simpleName

fun Int?.orZero(): Int = this ?: 0

fun Long?.orZero(): Long = this ?: 0L

fun Float?.orZero(): Float = this ?: 0F

fun Double?.orZero(): Double = this ?: 0.0

fun Boolean?.orFalse(): Boolean = this ?: false

fun Context.navigateToAppSettings() {
    this.startActivity(
        Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", this.packageName, null)
        )
    )
}

fun Context.showMessage(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}



