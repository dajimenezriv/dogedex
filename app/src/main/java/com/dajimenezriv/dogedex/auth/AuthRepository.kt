package com.dajimenezriv.dogedex.auth

import com.dajimenezriv.dogedex.models.User
import com.dajimenezriv.dogedex.api.APIResponseStatus
import com.dajimenezriv.dogedex.api.APIService
import com.dajimenezriv.dogedex.api.dto.LoginDTO
import com.dajimenezriv.dogedex.api.dto.SignUpDTO
import com.dajimenezriv.dogedex.api.dto.UserDTOMapper
import com.dajimenezriv.dogedex.api.makeNetworkCall
import java.lang.Exception
import javax.inject.Inject

interface AuthRepositoryInterface {
    suspend fun logIn(
        email: String,
        password: String,
    ): APIResponseStatus<User>

    suspend fun signUp(
        email: String,
        password: String,
        confirmPassword: String
    ): APIResponseStatus<User>
}

class AuthRepository @Inject constructor(
    private val apiService: APIService
) : AuthRepositoryInterface {
    override suspend fun signUp(
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
            val authAPIResponse = apiService.signUp(signUpDTO)

            if (!authAPIResponse.isSuccess) {
                throw Exception(authAPIResponse.message)
            }

            // now we need to parse the value data.user using another DTO
            val userDTO = authAPIResponse.data.user
            val userDTOMapper = UserDTOMapper()
            userDTOMapper.fromUserDTOtoUserDomain(userDTO)
        }
    }

    override suspend fun logIn(
        email: String,
        password: String,
    ): APIResponseStatus<User> {
        return makeNetworkCall {
            // we create a DTO, which is the way we have to receive or send data
            val loginDTO = LoginDTO(email, password)
            // we send the DTO using retrofit
            // the method signUp is a @POST that sends a @Body
            // that returns a signUpAPIResponse
            val authAPIResponse = apiService.login(loginDTO)

            if (!authAPIResponse.isSuccess) {
                throw Exception(authAPIResponse.message)
            }

            // now we need to parse the value data.user using another DTO
            val userDTO = authAPIResponse.data.user
            val userDTOMapper = UserDTOMapper()
            userDTOMapper.fromUserDTOtoUserDomain(userDTO)
        }
    }
}