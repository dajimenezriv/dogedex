package com.dajimenezriv.dogedex.api.dto

// all objects than receive data from internet are call dto
// data transfer object
// the server is going to receive these variables
// the only variable that changes is confirmPassword, so we use the @field:Json option
class LoginDTO(
    val email: String,
    val password: String,
)