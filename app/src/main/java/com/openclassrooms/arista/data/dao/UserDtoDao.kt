package com.openclassrooms.arista.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.openclassrooms.arista.data.entity.UserDto
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDtoDao {

    @Query("SELECT id FROM user ORDER BY id ASC LIMIT 1")
    suspend fun getFirstUserId(): Long?

    @Query("SELECT * FROM user WHERE id = :id")
    fun getUserById(id: Long): Flow<UserDto?>

    @Query("DELETE FROM user WHERE id = :id")
    suspend fun deleteUserById(id: Long)

    @Insert
    suspend fun insertUser(user: UserDto): Long

}