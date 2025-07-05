package com.openclassrooms.arista.ui.exercise

import androidx.lifecycle.ViewModel
import com.openclassrooms.arista.domain.model.Exercise
import com.openclassrooms.arista.domain.usecase.AddNewExerciseUseCase
import com.openclassrooms.arista.domain.usecase.DeleteExerciseUseCase
import com.openclassrooms.arista.domain.usecase.GetAllExercisesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

@HiltViewModel
class ExerciseViewModel @Inject constructor(
    private val getAllExercisesUseCase: GetAllExercisesUseCase,
    private val addNewExerciseUseCase: AddNewExerciseUseCase,
    private val deleteExerciseUseCase: DeleteExerciseUseCase
) : ViewModel() {
    private val _exercisesFlow = MutableStateFlow<List<Exercise>>(emptyList())
    val exercisesFlow: StateFlow<List<Exercise>> = _exercisesFlow.asStateFlow()

    init {
        viewModelScope.launch {
            loadAllExercises()
        }
    }

    suspend fun deleteExercise(exercise: Exercise) {
        deleteExerciseUseCase.execute(exercise)
        loadAllExercises()
    }

    private suspend fun loadAllExercises() {
        val exercises = getAllExercisesUseCase.execute()
        _exercisesFlow.value = exercises
    }

    suspend fun addNewExercise(exercise: Exercise) {
        addNewExerciseUseCase.execute(exercise)
        loadAllExercises()
    }
}
