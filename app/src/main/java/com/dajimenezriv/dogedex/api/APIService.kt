package com.dajimenezriv.dogedex.api

import com.dajimenezriv.dogedex.api.dto.AddDogToUserDTO
import com.dajimenezriv.dogedex.api.dto.LoginDTO
import com.dajimenezriv.dogedex.api.dto.SignUpDTO
import com.dajimenezriv.dogedex.api.responses.DogListAPIResponse
import com.dajimenezriv.dogedex.api.responses.AuthAPIResponse
import com.dajimenezriv.dogedex.api.responses.DefaultResponse
import com.dajimenezriv.dogedex.api.responses.DogAPIResponse
import retrofit2.http.*

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

    @Headers("${APIServiceInterceptor.NEEDS_AUTH_HEADER_KEY}: true")
    @POST("add_dog_to_user")
    suspend fun addDogToUser(@Body addDogToUserDTO: AddDogToUserDTO): DefaultResponse

    @Headers("${APIServiceInterceptor.NEEDS_AUTH_HEADER_KEY}: true")
    @GET("get_user_dogs")
    suspend fun getUserDogs(): DogListAPIResponse

    @GET("find_dog_by_ml_id")
    suspend fun getDogMyMlId(@Query("ml_id") mlId: String): DogAPIResponse
}
