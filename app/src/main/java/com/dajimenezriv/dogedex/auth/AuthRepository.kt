package com.dajimenezriv.dogedex.auth

import com.dajimenezriv.dogedex.models.User
import com.dajimenezriv.dogedex.api.APIResponseStatus
import com.dajimenezriv.dogedex.api.DogsAPI
import com.dajimenezriv.dogedex.api.dto.SignUpDTO
import com.dajimenezriv.dogedex.api.dto.UserDTOMapper
import com.dajimenezriv.dogedex.api.makeNetworkCall
import java.lang.Exception

class AuthRepository {
    suspend fun signUp(
        email: String,
        password: String,
        confirmPassword: String
    ): APIResponseStatus<User> {
        return makeNetworkCall {
            // we create a DTO, which is the way we have to receive or send data
            val signUpDTO = SignUpDTO(email, password, confirmPassword)
            // we send the DTO using retrofit
            // the method signUp is a @POST that sends a @Body
            // that returns a signUpAPIResponse
            val signUpAPIResponse = DogsAPI.retrofitService.signUp(signUpDTO)

            if (!signUpAPIResponse.isSuccess) {
                throw Exception(signUpAPIResponse.message)
            }

            // now we need to parse the value data.user using another DTO
            val userDTO = signUpAPIResponse.data.user
            val userDTOMapper = UserDTOMapper()
            userDTOMapper.fromUserDTOtoUserDomain(userDTO)
        }
    }
}