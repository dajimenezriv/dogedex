package com.dajimenezriv.dogedex.api.dto

import com.squareup.moshi.Json

// all objects than receive data from internet are call dto
// data transfer object
// the server is going to receive these variables
// the only variable that changes is confirmPassword, so we use the @field:Json option
class SignUpDTO(
    val email: String,
    val password: String,
    @field:Json(name = "password_confirmation") val confirmPassword: String
)