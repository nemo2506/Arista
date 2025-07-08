package com.openclassrooms.arista.domain.usecase

import com.openclassrooms.arista.data.repository.MissingUserIdException
import com.openclassrooms.arista.data.repository.SleepRepository
import com.openclassrooms.arista.domain.model.Sleep
import javax.inject.Inject

class GetAllSleepsUseCase @Inject constructor(
    private val sleepRepository: SleepRepository,
    private val userUseCase: GetUserUseCase
) {
    suspend fun execute(): List<Sleep> {
        return sleepRepository.getAllSleeps().filter {
            it.userId == (userUseCase.execute()?.id
                ?: throw MissingUserIdException())
        }
    }
}