package com.openclassrooms.arista.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.openclassrooms.arista.data.entity.ExerciseDto
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDtoDao {
    @Insert
    fun insertExercise(exercise: ExerciseDto): Long

    @Query("SELECT * FROM exercise WHERE userId = 1 ORDER BY startTime ASC") // hardcoded user ID
    fun getAllExercises(): Flow<List<ExerciseDto>>

    @Query("DELETE FROM exercise WHERE id = :id")
    fun deleteExerciseById(id: Long)
}