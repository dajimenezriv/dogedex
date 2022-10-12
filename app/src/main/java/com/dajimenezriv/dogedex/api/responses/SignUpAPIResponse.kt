package com.dajimenezriv.dogedex.api.responses

import com.squareup.moshi.Json

class SignUpAPIResponse(
    val message: String,
    @field:Json(name = "is_success") val isSuccess: Boolean,
    val data: UserResponse
)