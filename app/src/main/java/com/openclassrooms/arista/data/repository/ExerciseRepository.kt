package com.openclassrooms.arista.data.repository

import com.openclassrooms.arista.data.dao.ExerciseDtoDao
import com.openclassrooms.arista.domain.model.Exercise
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
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
    fun addExercise(exercise: Exercise): Flow<Result<Unit>> = flow {
        exerciseDao.insertExercise(exercise.toDto())
        emit(Result.success(Unit))
    }.catch { e ->
        emit(Result.failure(ExerciseRepositoryException("Failed to add exercise", e)))
    }

    // Del a exercise
    fun deleteExercise(exercise: Exercise): Flow<Result<Unit>> = flow {
        val id = exercise.id ?: throw MissingExerciseIdException()
        exerciseDao.deleteExerciseById(id)
        emit(Result.success(Unit))
    }.catch { e ->
        emit(Result.failure(ExerciseRepositoryException("Failed to del exercise", e)))
    }
}
