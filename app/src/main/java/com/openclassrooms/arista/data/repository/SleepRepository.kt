package com.openclassrooms.arista.data.repository

import com.openclassrooms.arista.data.dao.SleepDtoDao
import com.openclassrooms.arista.data.dao.UserDtoDao
import com.openclassrooms.arista.domain.model.Sleep
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class SleepRepository(
    private val sleepDao: SleepDtoDao,
    private val userDtoDao: UserDtoDao
) {

    // Get all sleeps
    fun getAllSleeps(): Flow<List<Sleep>> = flow {
        val userId = userDtoDao.getFirstUserId()
            ?: throw SleepRepositoryException("No user found")

        sleepDao.getAllSleepByUserId(userId)
            .map { dtoList -> dtoList.map { Sleep.fromDto(it) } }
            .collect { emit(it) }
    }.catch { e ->
        throw SleepRepositoryException("Failed to fetch sleeps", e)
    }

    // Add a new sleep
    suspend fun addSleep(sleep: Sleep) {
        try {
            sleepDao.insertSleep(sleep.toDto())
        } catch (e: Exception) {
            throw SleepRepositoryException("Failed to add sleep", e)
        }
    }

    // Delete a sleep
    suspend fun deleteSleep(sleep: Sleep) {
        try {
            val id = sleep.id ?: throw MissingSleepIdException()
            sleepDao.deleteSleepById(id)
        } catch (e: Exception) {
            throw SleepRepositoryException("Failed to delete sleep", e)
        }
    }
}
