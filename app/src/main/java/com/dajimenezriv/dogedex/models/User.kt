package com.dajimenezriv.dogedex.models

data class User(
    val id: Long,
    val email: String,
    val authenticationToken: String,
)