package com.rzgonz.sawitpro.core

/**
 * Created by rzgonz on 13/03/23.
 *
 */
sealed class Resource<T>(val data: T? = null, val error: String? = null) {
    class Success<T>(data: T) : Resource<T>(data = data)
    class Error<T>(error: String, data: T? = null) : Resource<T>(data = data, error = error)
}