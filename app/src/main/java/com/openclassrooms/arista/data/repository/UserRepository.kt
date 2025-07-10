package com.openclassrooms.arista.data.repository

import com.openclassrooms.arista.data.dao.UserDtoDao
import com.openclassrooms.arista.data.entity.UserDto
import com.openclassrooms.arista.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class UserRepository(private val userDao: UserDtoDao) {

    // Get all first user
    fun getFirstUser(): Flow<User?> {
        return userDao.getFirstUser()
            .map { dto -> User.fromDto(dto) } // safely converts or returns null
            .catch { e ->
                throw UserRepositoryException("Failed to fetch User", e)
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
