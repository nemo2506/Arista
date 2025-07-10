package com.openclassrooms.arista.domain.usecase

import android.util.Log
import com.openclassrooms.arista.data.repository.ExerciseRepository
import com.openclassrooms.arista.domain.model.Exercise
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddNewExerciseUseCase @Inject constructor(
    private val exerciseRepository: ExerciseRepository
) {
    fun execute(exercise: Exercise): Flow<Result<Unit>> {
        Log.d("MARC2", "execute: $exercise")
        return exerciseRepository.addExercise(exercise)
    }
}