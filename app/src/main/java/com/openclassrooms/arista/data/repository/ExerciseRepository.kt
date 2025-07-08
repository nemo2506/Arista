package com.openclassrooms.arista.data.repository

import android.util.Log
import com.openclassrooms.arista.data.dao.ExerciseDtoDao
import com.openclassrooms.arista.domain.model.Exercise
import kotlinx.coroutines.flow.first

class ExerciseRepository(private val exerciseDao: ExerciseDtoDao) {

    // Get all exercises
    suspend fun getAllExercises(): List<Exercise> {
        return try {
            exerciseDao.getAllExercises()
                .first()
                .map { Exercise.fromDto(it) }
        } catch (e: Exception) {
            throw ExerciseRepositoryException("Failed to fetch exercises", e)
        }
    }

    // Add a new exercise
    suspend fun addExercise(exercise: Exercise) {
        try {
            exerciseDao.insertExercise(exercise.toDto())
        } catch (e: Exception) {
            throw ExerciseRepositoryException("Failed to add exercise", e)
        }
    }

    // Delete an exercise
    suspend fun deleteExercise(exercise: Exercise) {
        try {
            val id = exercise.id ?: throw MissingExerciseIdException()
            exerciseDao.deleteExerciseById(id)
        } catch (e: Exception) {
            throw ExerciseRepositoryException("Failed to delete exercise", e)
        }
    }
}
