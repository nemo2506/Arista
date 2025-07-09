package com.openclassrooms.arista.domain.usecase

import com.openclassrooms.arista.data.repository.ExerciseRepository
import com.openclassrooms.arista.domain.model.Exercise
import com.openclassrooms.arista.domain.model.ExerciseReportModel
import javax.inject.Inject

class GetAllExercisesUseCase @Inject constructor(
    private val exerciseRepository: ExerciseRepository
) {
    suspend fun execute(): Result<ExerciseReportModel> {
        return try {
            val exercises = exerciseRepository.getAllExercises()
            val model = ExerciseReportModel(exercises=exercises)
            Result.Success(model)
        } catch (error: Exception) {
            Result.Failure(error.message)
        }
    }
}