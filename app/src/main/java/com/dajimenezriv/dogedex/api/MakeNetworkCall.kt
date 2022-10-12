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
            APIResponseStatus.Error(
                when (e.message) {
                    "sign_up_error" -> R.string.error_sign_up
                    "sign_in_error" -> R.string.error_sign_in
                    "user_already_exists" -> R.string.user_already_exists
                    else -> R.string.error_unknown_error
                }
            )
        }
    }
}