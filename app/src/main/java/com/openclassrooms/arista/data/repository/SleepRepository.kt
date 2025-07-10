package com.openclassrooms.arista.data.repository

import com.openclassrooms.arista.data.dao.SleepDtoDao
import com.openclassrooms.arista.domain.model.Sleep
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class SleepRepository(private val sleepDao: SleepDtoDao) {

    // Get all sleeps
    fun getAllSleeps(): Flow<List<Sleep>> {
        return sleepDao.getAllSleep()
            .map { dtoList ->
                dtoList.map { Sleep.fromDto(it) }
            }
            .catch { e ->
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
