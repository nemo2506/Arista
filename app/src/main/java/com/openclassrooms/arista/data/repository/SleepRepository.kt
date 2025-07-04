package com.openclassrooms.arista.data.repository

import com.openclassrooms.arista.data.dao.SleepDtoDao
import com.openclassrooms.arista.domain.model.Sleep
import kotlinx.coroutines.flow.first

class SleepRepository(private val sleepDao: SleepDtoDao) {

    // Get all sleeps
    suspend fun getAllSleeps(): List<Sleep> {
        return try {
            sleepDao.getAllSleep()
                .first()
                .map { Sleep.fromDto(it) }
        } catch (e: Exception) {
            throw SleepRepositoryException("Failed to fetch sleeps", e)
        }
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
