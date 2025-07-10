package com.openclassrooms.arista.data.repository

import com.openclassrooms.arista.data.dao.UserDtoDao
import com.openclassrooms.arista.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class UserRepository(private val userDtoDao: UserDtoDao) {

    // Get all first user
    fun getFirstUser(): Flow<User?> = flow {
        val id = userDtoDao.getFirstUserId()
            ?: throw UserRepositoryException("No user found")

        userDtoDao.getUserById(id)
            .map { dto -> dto?.let { User.fromDto(it) } }
            .collect { user -> emit(user) }
    }.catch { e ->
        throw UserRepositoryException("Failed to fetch User", e)
    }


    // Add a new user
    suspend fun addUser(user: User) {
        try {
            userDtoDao.insertUser(user.toDto())
        } catch (e: Exception) {
            throw UserRepositoryException("Failed to add user", e)
        }
    }

    // Delete a user
    suspend fun deleteUser(user: User) {
        try {
            val id = user.id ?: throw MissingUserIdException()
            userDtoDao.deleteUserById(id)
        } catch (e: Exception) {
            throw UserRepositoryException("Failed to delete user", e)
        }
    }
}
