package com.openclassrooms.arista.data.repository

import com.openclassrooms.arista.data.dao.UserDtoDao
import com.openclassrooms.arista.domain.model.User
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull

class UserRepository(private val userDao: UserDtoDao) {

    // Get all users
    suspend fun getAllUsers(): List<User> {
        try {
            return userDao.getAllUser()
                .first()
                .map { User.fromDto(it) }
        } catch (e: Exception) {
            throw UserRepositoryException("Failed to fetch users", e)
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
