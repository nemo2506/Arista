package com.openclassrooms.arista.domain.usecase

import com.openclassrooms.arista.data.repository.ExerciseRepository
import com.openclassrooms.arista.domain.model.Exercise
import com.openclassrooms.arista.domain.model.ExerciseCategory
import java.time.LocalDateTime
import javax.inject.Inject

class AddNewExerciseUseCase @Inject constructor(
    private val exerciseRepository: ExerciseRepository,
    private val userUseCase: GetUserUseCase
) {
    suspend fun execute(exercise: List<Any>) {
        val userId = userUseCase.execute()?.id
        ?: throw IllegalStateException("User ID is null")
        exerciseRepository.addExercise(Exercise(
            startTime = exercise[0] as LocalDateTime,
            duration = exercise[1] as Int,
            category = exercise[2] as ExerciseCategory,
            intensity = exercise[3] as Int,
            userId = userId as Long
        ))
    }
}