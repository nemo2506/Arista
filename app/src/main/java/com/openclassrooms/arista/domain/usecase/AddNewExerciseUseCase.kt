package com.openclassrooms.arista.domain.usecase

import com.openclassrooms.arista.data.repository.ExerciseRepository
import com.openclassrooms.arista.domain.model.Exercise
import java.time.LocalDateTime
import javax.inject.Inject

class AddNewExerciseUseCase @Inject constructor(
    private val exerciseRepository: ExerciseRepository
) {
    suspend fun execute(exercise: Exercise) {
        try {
            exerciseRepository.addExercise(exercise)
        } catch (e: Exception) {
            throw IllegalStateException("User ID is null")
        }

    }
}