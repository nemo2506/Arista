package com.openclassrooms.arista.domain.usecase

import com.openclassrooms.arista.data.repository.SleepRepository
import com.openclassrooms.arista.domain.model.Sleep
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllSleepsUseCase @Inject constructor(
    private val sleepRepository: SleepRepository
) {
    fun execute(): Flow<List<Sleep>> {
        return sleepRepository.getAllSleeps()
    }
}

