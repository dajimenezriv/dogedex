package com.dajimenezriv.dogedex.api

import com.dajimenezriv.dogedex.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.UnknownHostException

suspend fun <T> makeNetworkCall(callback: suspend () -> T): APIResponseStatus<T> {
    return withContext(Dispatchers.IO) {
        try {
            APIResponseStatus.Success(callback())
        } catch (e: UnknownHostException) {
            APIResponseStatus.Error(R.string.error_unknown_host_exception)
        } catch (e: Exception) {
            APIResponseStatus.Error(R.string.error_unknown_error)
        }
    }
}