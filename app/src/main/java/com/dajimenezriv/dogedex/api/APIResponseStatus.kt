package com.dajimenezriv.dogedex.api

sealed class APIResponseStatus<T> {
    // if doesn't have status it can be an object
    class Loading<T>(): APIResponseStatus<T>()
    class Success<T>(val data: T): APIResponseStatus<T>()
    class Error<T>(val messageId: Int): APIResponseStatus<T>()
}
