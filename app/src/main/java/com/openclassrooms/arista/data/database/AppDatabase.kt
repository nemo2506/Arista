package com.openclassrooms.arista.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.openclassrooms.arista.data.dao.ExerciseDtoDao
import com.openclassrooms.arista.data.entity.ExerciseDto

@Database(entities = [UserDto::class, SleepDto::class, ExerciseDto::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
 abstract fun userDtoDao(): UserDtoDao
 abstract fun sleepDtoDao(): SleepDtoDao
 abstract fun exerciseDtoDao(): ExerciseDtoDao


 companion object {
 @Volatile
 private var INSTANCE: AppDatabase? = null


 fun getDatabase(context: Context): AppDatabase {
 return INSTANCE ?: synchronized(this) {
 val instance = Room.databaseBuilder(
 context.applicationContext,
 AppDatabase::class.java,
 "app_database"
 ).build()
 INSTANCE = instance
 instance
 }
 }
 }
}