package com.dajimenezriv.dogedex.api.dto

import com.dajimenezriv.dogedex.models.User

class UserDTOMapper {
    fun fromUserDTOtoUserDomain(userDTO: UserDTO): User {
        return User(
            userDTO.id,
            userDTO.email,
            userDTO.authenticationToken,
        )
    }
}