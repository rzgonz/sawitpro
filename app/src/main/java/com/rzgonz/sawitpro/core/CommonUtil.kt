package com.rzgonz.sawitpro.core

fun safeLaunch(block: () -> Unit) = try {
    block.invoke()
} catch (e: Exception) {
    e.printStackTrace()
}

inline fun <reified T> safeLaunchWithResult(default: T, block: () -> T): T = try {
    block()
} catch (e: Exception) {
    e.printStackTrace()
    default
}