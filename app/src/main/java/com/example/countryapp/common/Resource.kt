package com.example.countryapp.common

sealed class Resource<T>(val message : String? = null, val data : T? = null) {

    class OnLoading<T> : Resource<T>()
    class Success<T>(data: T) : Resource<T>(data = data)
    class Error<T>(message: String) : Resource<T>(message = message)
}