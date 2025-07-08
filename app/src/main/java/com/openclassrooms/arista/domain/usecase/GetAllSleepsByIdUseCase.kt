package com.openclassrooms.arista.domain.usecase

import com.openclassrooms.arista.data.repository.SleepRepository
import com.openclassrooms.arista.data.repository.UserRepository
import com.openclassrooms.arista.domain.model.Sleep
import javax.inject.Inject

class GetAllSleepsByIdUseCase @Inject constructor(
    private val sleepRepository: SleepRepository,
    private val userRepository: UserRepository
) {
    suspend fun execute(): List<Sleep> {
        val userId = userRepository.getAllUsers().firstOrNull()?.id
        return sleepRepository.getAllSleeps().filter { it.userId == userId }
    }
}