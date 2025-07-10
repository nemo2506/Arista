package com.openclassrooms.arista.domain.usecase

import com.openclassrooms.arista.data.repository.ExerciseRepository
import com.openclassrooms.arista.domain.model.Exercise
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class DeleteExerciseUseCase @Inject constructor(
    private val repository: ExerciseRepository
) {
    fun execute(exercise: Exercise) {
        repository.deleteExercise(exercise)
    }
}