package com.dajimenezriv.dogedex.api.responses

import com.squareup.moshi.Json

class DogListAPIResponse(
    val message: String,
    @field:Json(name="is_success") val isSuccess: Boolean,
    val data: DogListResponse
) {

}