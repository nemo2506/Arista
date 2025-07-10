package com.openclassrooms.arista.domain.model

import com.openclassrooms.arista.data.entity.UserDto

data class User(
    var id: Long,
    var name: String,
    var email: String,
    var password: String
) {
    companion object {
        fun fromDto(dto: UserDto?): User? {
            return dto?.let {
                User(
                    id = it.id,
                    name = it.name,
                    email = it.email,
                    password = it.password
                )
            }
        }
    }

    fun toDto(): UserDto {
        return UserDto(
            id = this.id,
            name = this.name,
            email = this.email,
            password = this.password
        )
    }
}