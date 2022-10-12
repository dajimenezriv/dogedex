package com.dajimenezriv.dogedex.api

import com.dajimenezriv.dogedex.BASE_URL
import com.dajimenezriv.dogedex.api.dto.LoginDTO
import com.dajimenezriv.dogedex.api.dto.SignUpDTO
import com.dajimenezriv.dogedex.api.responses.DogListAPIResponse
import com.dajimenezriv.dogedex.api.responses.AuthAPIResponse
import retrofit2.Retrofit
// parse json in an object that we can use
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

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

    @POST("sign_up")
    suspend fun signUp(@Body signUpDTO: SignUpDTO): AuthAPIResponse

    @POST("sign_in")
    suspend fun login(@Body loginDTO: LoginDTO): AuthAPIResponse
}

// this is the way with retrofit
object DogsAPI {
    // lazy is to tell the app, don't create this until we call it the first time
    val retrofitService: APIService by lazy {
        retrofit.create(APIService::class.java)
    }
}
