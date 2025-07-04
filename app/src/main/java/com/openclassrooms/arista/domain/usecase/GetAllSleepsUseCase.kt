package com.openclassrooms.arista.domain.usecase

import com.openclassrooms.arista.data.repository.SleepRepository
import com.openclassrooms.arista.domain.model.Sleep
import javax.inject.Inject

class GetAllSleepsUseCase @Inject constructor(private val sleepRepository: SleepRepository) {
    suspend fun execute(): List<Sleep> {
        return sleepRepository.getAllSleeps()
    }
}