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
        observeExercises()
    }

    private fun observeExercises() {
        viewModelScope.launch {
            getAllExercisesUseCase.execute()
                .catch {
                    _uiState.update { it.copy(isExerciseReady = false) }
                }
                .collect { exercises ->
                    if (exercises.isEmpty()) {
                        _uiState.update { it.copy(isExerciseReady = false) }
                    } else {
                        _uiState.update { it.copy(exercises = exercises) }
                    }
                }
        }
    }

    fun reset() {
        _uiState.update {
            it.copy(
                isExerciseReady = null,
                isExerciseAdded = null,
                isExerciseDeleted = null
            )
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
    fun addExercise(
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
                    _uiState.update { it.copy(isExerciseAdded = false) }
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
                    _uiState.update { it.copy(isExerciseDeleted = false) }
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

/**
 * Data class that represents the UI state for the exercise screen.
 *
 * @param exercises List of exercises to display.
 */
data class UiState(
    var exercises: List<Exercise>? = null,
    var isExerciseReady: Boolean? = null,
    var isExerciseAdded: Boolean? = null,
    var isExerciseDeleted: Boolean? = null
)