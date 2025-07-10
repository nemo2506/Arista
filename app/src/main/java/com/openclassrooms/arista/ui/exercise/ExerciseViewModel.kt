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
import com.openclassrooms.arista.domain.model.ExerciseCategory
import kotlinx.coroutines.flow.catch
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
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        loadAllExercises()
    }

    fun loadAllExercises() {
        viewModelScope.launch {
            getAllExercisesUseCase.execute()
                .catch { error ->
                    _uiState.update { it.copy(message = error.message ?: "Unknown error") }
                }
                .collect { exercises ->
                    _uiState.update { it.copy(exercises = exercises, message = null) }
                }
        }
    }


    fun add(
        startTime: LocalDateTime,
        duration: Int,
        category: ExerciseCategory,
        intensity: Int
    ) {
        val exercise = Exercise(
            startTime = startTime,
            duration = duration,
            category = category,
            intensity = intensity,
            userId = 1
        )
        viewModelScope.launch {
            addNewExerciseUseCase.execute(exercise).collect { result ->
                if (result.isFailure) {
                    _uiState.update { it.copy(message = result.exceptionOrNull()?.message) }
                } else {
                    loadAllExercises() // refresh
                }
            }
        }
    }

    fun deleteExercise(exercise: Exercise) {
        viewModelScope.launch {
            deleteExerciseUseCase.execute(exercise).collect { result ->
                if (result.isFailure) {
                    _uiState.update { it.copy(message = result.exceptionOrNull()?.message) }
                } else {
                    loadAllExercises() // refresh
                }
            }
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

    fun validateDuration(duration: String): Boolean {
        return duration.isNotBlank()
    }

    fun validateIntensity(intensity: String): IntensityValidationResult {
        if (intensity.isBlank()) {
            return IntensityValidationResult.Blank
        }
        return try {
            val value = intensity.toInt()
            if (value in 1..10) {
                IntensityValidationResult.Valid
            } else {
                IntensityValidationResult.OutOfRange
            }
        } catch (e: NumberFormatException) {
            IntensityValidationResult.InvalidNumber
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

sealed class IntensityValidationResult {
    object Valid : IntensityValidationResult()
    object Blank : IntensityValidationResult()
    object InvalidNumber : IntensityValidationResult()
    object OutOfRange : IntensityValidationResult()
}
