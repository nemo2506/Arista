package com.openclassrooms.arista.domain.usecase

import com.openclassrooms.arista.data.repository.ExerciseRepository
import com.openclassrooms.arista.data.repository.UserRepository
import com.openclassrooms.arista.domain.model.Exercise
import javax.inject.Inject

class GetAllExercisesUseCase @Inject constructor(
    private val exerciseRepository: ExerciseRepository,
    private val userRepository: UserRepository
) {
    suspend fun execute(): List<Exercise> {
        val userId = userRepository.getAllUsers().firstOrNull()?.id
        return exerciseRepository.getAllExercises().filter { it.userId == userId }
    }
}