package com.rzgonz.sawitpro.core

import android.util.Log


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



