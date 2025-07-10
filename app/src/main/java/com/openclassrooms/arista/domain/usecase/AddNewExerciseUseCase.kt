package com.openclassrooms.arista.domain.usecase

import com.openclassrooms.arista.data.repository.ExerciseRepository
import com.openclassrooms.arista.domain.model.Exercise
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddNewExerciseUseCase @Inject constructor(
    private val exerciseRepository: ExerciseRepository
) {
    fun execute(exercise: Exercise): Flow<Result<Unit>> {
        return exerciseRepository.addExercise(exercise)
    }
}