package com.dajimenezriv.dogedex.api.dto

import com.squareup.moshi.Json

// we create an intermediate DTO to convert the field data returned in SignUpAPIResponse
// to a variable User
class UserDTO(
    val id: Long,
    val email: String,
    @field:Json(name = "authentication_token") val authenticationToken: String,
)