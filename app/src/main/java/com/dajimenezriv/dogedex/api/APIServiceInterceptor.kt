package com.dajimenezriv.dogedex.api

import okhttp3.Interceptor
import okhttp3.Response
import java.lang.RuntimeException

// object doesn't have instances
object APIServiceInterceptor: Interceptor {
    const val NEEDS_AUTH_HEADER_KEY = "needs_authentication"

    private var sessionToken: String? = null

    fun setSessionToken(sessionToken: String) {
        this.sessionToken = sessionToken
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestBuilder = request.newBuilder()
        // if the request has this header it means that we need the token in the header
        // in that case, it's going to check whether the token is null or not
        // if we need a token but the token is null, throw an exception
        // otherwise add the token to the header
        if (request.header(NEEDS_AUTH_HEADER_KEY) != null) {
            if (sessionToken == null) {
                throw RuntimeException("Need to be authenticated to perform this opearation.")
            } else {
                requestBuilder.addHeader("AUTH-TOKEN", sessionToken!!)
            }
        }
        return chain.proceed(requestBuilder.build())
    }

}