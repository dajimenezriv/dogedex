package com.dajimenezriv.dogedex.api

import com.dajimenezriv.dogedex.BASE_URL
import com.dajimenezriv.dogedex.api.responses.DogListAPIResponse
import retrofit2.Retrofit
// parse json in an object that we can use
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create())
    .build()

interface APIService {
    /*
    The structure that this method is returns is as follows:
    {
        "message": "success",
        "is_success": true,
        "data": {
            "dogs": [
                INFO DOGS
            ]
        }
    }
     */
    @GET("dogs")
    suspend fun getAllDogs(): DogListAPIResponse
}

// this is the way with retrofit
object DogsAPI {
    // lazy is to tell the app, don't create this until we call it the first time
    val retrofitService: APIService by lazy {
        retrofit.create(APIService::class.java)
    }
}
