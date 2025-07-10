package com.openclassrooms.arista.data.repository

import com.openclassrooms.arista.data.dao.UserDtoDao
import com.openclassrooms.arista.data.entity.UserDto
import com.openclassrooms.arista.domain.model.User
import kotlinx.coroutines.flow.first

class UserRepository(private val userDao: UserDtoDao) {

    // Get all users
    suspend fun getFirstUser(): UserDto? {
        return try {
            userDao.getFirstUser().first()
        } catch (e: Exception) {
            null // or log the error
        }
    }

    // Add a new user
    suspend fun addUser(user: User) {
        try {
            userDao.insertUser(user.toDto())
        } catch (e: Exception) {
            throw UserRepositoryException("Failed to add user", e)
        }
    }

    // Delete a user
    suspend fun deleteUser(user: User) {
        try {
            val id = user.id ?: throw MissingUserIdException()
            userDao.deleteUserById(id)
        } catch (e: Exception) {
            throw UserRepositoryException("Failed to delete user", e)
        }
    }
}
