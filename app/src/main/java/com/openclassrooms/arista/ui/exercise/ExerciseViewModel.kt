package com.openclassrooms.arista.ui.exercise

import androidx.lifecycle.ViewModel
import com.openclassrooms.arista.domain.model.Exercise
import com.openclassrooms.arista.domain.usecase.AddNewExerciseUseCase
import com.openclassrooms.arista.domain.usecase.DeleteExerciseUseCase
import com.openclassrooms.arista.domain.usecase.GetAllExercisesUseCase
import com.openclassrooms.arista.domain.usecase.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.openclassrooms.arista.domain.model.ExerciseCategory
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@HiltViewModel
class ExerciseViewModel @Inject constructor(
    private val getAllExercisesUseCase: GetAllExercisesUseCase,
    private val addNewExerciseUseCase: AddNewExerciseUseCase,
    private val deleteExerciseUseCase: DeleteExerciseUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())

    //    val exercisesFlow: StateFlow<List<Exercise>> = _uiState.asStateFlow()
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            loadAllExercises()
        }
    }

    suspend fun deleteExercise(exercise: Exercise) {
        deleteExerciseUseCase.execute(exercise)
        loadAllExercises()
    }

    //    private suspend fun loadAllExercises() {
//        val exercises = getAllExercisesUseCase.execute()
//        _exercisesFlow.value = exercises
//    }
    private fun loadAllExercises() {
        viewModelScope.launch() {
            // Attempt to log in and update UI state based on the result
            when (val update = getAllExercisesUseCase.execute()) {

                // If balance fails, update state with failure message
                is Result.Failure -> {
                    _uiState.update { currentState ->
                        currentState.copy(
                            message = update.message
                        )
                    }
                }

                // If balance is successful, update state with login success
                is Result.Success -> {
                    _uiState.update { currentState ->
                        currentState.copy(
                            exercises = update.value.exercises
                        )
                    }
                }
            }
        }
    }

    fun add(
        startTime: LocalDateTime,
        duration: Int,
        category: ExerciseCategory,
        intensity: Int
    ) {
        viewModelScope.launch {
            addNewExerciseUseCase.execute(
                Exercise(
                    startTime = startTime,
                    duration = duration,
                    category = category,
                    intensity = intensity,
                    userId = 1L // hardcoded user ID
                )
            )
            loadAllExercises()
        }
    }

    /**
     * @param intensity Boolean indicating if the identifier is not empty.
     */
    fun validateIntensityNotBlank(intensity: String) {
        _uiState.update { currentState ->
            currentState.copy(
                isIntensityIntervalReady = intensity.isNotBlank()
            )
        }
    }

    /**
     * @param intensity Boolean indicating if the identifier is between 1-10.
     */
    fun validateIntensityInterval(intensity: String) {
        var interval: Boolean? = null
        var format: Boolean = false
        try {
            val intensityValue = intensity.toInt()
            if (intensityValue !in 1..10) {
                interval = false
            } else {
                format = true
            }
        } catch (e: NumberFormatException) {
            format = true
        }
        _uiState.update { currentState ->
            currentState.copy(
                isIntensityIntervalReady = interval,
                isIntensityFormatReady = !format
            )
        }
    }

    /**
     * @param duration Boolean indicating if the duration is not empty.
     */
    fun validateDuration(duration: String) {
        _uiState.update { currentState ->
            currentState.copy(
                isDurationReady = duration.isNotBlank()
            )
        }
    }
}

/**
 * Data class that represents the UI state for the login screen.
 *
 * Holds information about whether the user data is ready,
 * whether the login was successful, if the screen is loading,
 * and any error messages that may have occurred during login.
 */
data class UiState(
    var exercises: List<Exercise>? = null,
    var isIntensityIntervalReady: Boolean? = null,
    var isIntensityFormatReady: Boolean? = null,
    var isDurationReady: Boolean? = null,
    var message: String? = null
)