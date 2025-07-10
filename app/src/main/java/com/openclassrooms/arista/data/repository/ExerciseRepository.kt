package com.openclassrooms.arista.data.repository

import com.openclassrooms.arista.data.dao.ExerciseDtoDao
import com.openclassrooms.arista.domain.model.Exercise
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class ExerciseRepository(private val exerciseDao: ExerciseDtoDao) {

    // Get all exercises
    fun getAllExercises(): Flow<List<Exercise>> {
        return exerciseDao.getAllExercises()
            .map { dtoList ->
                dtoList.map { Exercise.fromDto(it) }
            }
            .catch { e ->
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
