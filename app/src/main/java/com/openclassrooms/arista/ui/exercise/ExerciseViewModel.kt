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

/**
 * ViewModel responsible for managing and exposing exercise-related UI state.
 *
 * Handles business logic such as loading, adding, and deleting exercises.
 * It also validates input fields like intensity and duration.
 *
 * @param getAllExercisesUseCase Use case to fetch all exercises.
 * @param addNewExerciseUseCase Use case to add a new exercise.
 * @param deleteExerciseUseCase Use case to delete an exercise.
 */
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

    /**
     * Loads all exercises from the repository and updates the UI state.
     * Any errors encountered are captured and reflected in the UI state.
     */
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

    /**
     * Adds a new exercise using the provided parameters.
     * On success, the list of exercises is refreshed.
     *
     * @param startTime The start time of the exercise.
     * @param duration The duration of the exercise in minutes.
     * @param category The category of the exercise.
     * @param intensity The intensity of the exercise (1 to 10).
     */
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

    /**
     * Deletes the specified exercise and refreshes the exercise list.
     *
     * @param exercise The exercise to be deleted.
     */
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
     * Validates whether the duration input is not blank.
     *
     * @param duration The duration input string.
     * @return `true` if not blank, `false` otherwise.
     */
    fun validateDuration(duration: String): Boolean {
        return duration.isNotBlank()
    }

    /**
     * Validates the intensity string based on three cases:
     * - Blank input
     * - Not a number
     * - Number not in the range 1..10
     *
     * @param intensity The intensity input string.
     * @return A [IntensityValidationResult] indicating the result.
     */
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
 * Data class that represents the UI state for the exercise screen.
 *
 * @param exercises List of exercises to display.
 * @param message Optional error or status message for the UI.
 */
data class UiState(
    var exercises: List<Exercise>? = null,
    var message: String? = null
)

/**
 * Represents the result of validating an intensity input string.
 */
sealed class IntensityValidationResult {
    /** Indicates the input is a valid number between 1 and 10. */
    object Valid : IntensityValidationResult()

    /** Indicates the input was blank. */
    object Blank : IntensityValidationResult()

    /** Indicates the input was not a valid number. */
    object InvalidNumber : IntensityValidationResult()

    /** Indicates the input number was outside the 1..10 range. */
    object OutOfRange : IntensityValidationResult()
}
