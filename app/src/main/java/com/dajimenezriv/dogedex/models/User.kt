package com.dajimenezriv.dogedex.models

import android.app.Activity
import android.content.Context

data class User(
    val id: Long,
    val email: String,
    val authenticationToken: String,
) {
    companion object {
        private const val AUTH_PREFS = "auth_prefs"

        // save session
        fun setLoggedInUser(activity: Activity, user: User) {
            activity.getSharedPreferences(AUTH_PREFS, Context.MODE_PRIVATE).also {
                it.edit()
                    .putLong("id", user.id)
                    .putString("email", user.email)
                    .putString("authenticationToken", user.authenticationToken)
                    .apply()
            }
        }

        fun getLoggedInUser(activity: Activity): User? {
            // if the user is null, then the session is not initialized
            // if prefs doesn't exist, user doesn't exist, return null then
            val prefs =
                activity.getSharedPreferences(AUTH_PREFS, Context.MODE_PRIVATE) ?: return null

            val userId = prefs.getLong("id", 0)
            if (userId == 0L) return null

            return User(
                userId,
                prefs.getString("email", "") ?: "",
                prefs.getString("authenticationToken", "") ?: "",
            )
        }
    }
}