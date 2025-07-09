package com.openclassrooms.arista.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.openclassrooms.arista.data.entity.UserDto
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDtoDao {
    @Insert
    suspend fun insertUser(user: UserDto): Long

    @Query("SELECT * FROM user ORDER BY id ASC LIMIT 1")
    fun getFirstUser(): Flow<UserDto>
//    fun getAllUser(): Flow<List<UserDto>>

    @Query("DELETE FROM user WHERE id = :id")
    suspend fun deleteUserById(id: Long)
}