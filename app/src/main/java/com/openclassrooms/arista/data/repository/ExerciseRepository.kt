package com.openclassrooms.arista.data.repository

import com.openclassrooms.arista.data.dao.ExerciseDtoDao
import com.openclassrooms.arista.data.dao.UserDtoDao
import com.openclassrooms.arista.domain.model.Exercise
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class ExerciseRepository(
    private val exerciseDao: ExerciseDtoDao,
    private val userDtoDao: UserDtoDao
) {

    // Get all exercises
    fun getAllExercises(): Flow<List<Exercise>> = flow {
        val userId = userDtoDao.getFirstUserId()
            ?: return@flow emit(emptyList())

        exerciseDao.getAllExercisesByUserId(userId)
            .map { dtoList -> dtoList.map { Exercise.fromDto(it) } }
            .collect { emit(it) }
    }.catch { e ->
        emit(emptyList())
    }


    // Add a new exercise
    fun addExercise(exercise: Exercise): Flow<Result<Unit>> = flow {
        val userId = userDtoDao.getFirstUserId()
            ?: throw ExerciseRepositoryException("No user found for assigning to exercise")
        val exerciseWithUser = exercise.copy(userId = userId)
        exerciseDao.insertExercise(exerciseWithUser.toDto())
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
