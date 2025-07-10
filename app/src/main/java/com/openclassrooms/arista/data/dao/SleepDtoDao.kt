package com.openclassrooms.arista.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.openclassrooms.arista.data.entity.SleepDto
import kotlinx.coroutines.flow.Flow

@Dao
interface SleepDtoDao {
    @Insert
    suspend fun insertSleep(sleep: SleepDto): Long

//    @Query(
//        """
//    SELECT * FROM sleep
//    WHERE userId = (
//        SELECT id FROM user ORDER BY id ASC LIMIT 1
//    )
//    ORDER BY startTime ASC
//"""
//    )
//    fun getAllSleep(): Flow<List<SleepDto>>

    @Query("SELECT * FROM sleep WHERE userId = :userId")
    fun getAllSleepByUserId(userId: Long): Flow<List<SleepDto>>

    @Query("DELETE FROM sleep WHERE id = :id")
    suspend fun deleteSleepById(id: Long)
}