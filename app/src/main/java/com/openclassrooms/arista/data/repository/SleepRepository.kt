package com.openclassrooms.arista.data.repository

import com.openclassrooms.arista.data.dao.SleepDtoDao
import com.openclassrooms.arista.domain.model.Sleep
import kotlinx.coroutines.flow.first

class SleepRepository(private val sleepDao: SleepDtoDao) {

    // Get all sleeps
    suspend fun getAllSleeps(): List<Sleep> {
        return sleepDao.getAllSleep()
            .first() // Collect the first emission of the Flow
            .map { Sleep.fromDto(it) } // Convert every DTO in Sleep
    }

    // Add a new sleep
    suspend fun addSleep(sleep: Sleep) {
        sleepDao.insertSleep(sleep.toDto())
    }

    // Delete an sleep
    suspend fun deleteSleep(sleep: Sleep) {
        // If there is no id, you can raise an exception and catch it in the use case and viewmodel
        sleep.id?.let {
            sleepDao.deleteSleepById(
                id = sleep.id,
            )
        }
    }
}