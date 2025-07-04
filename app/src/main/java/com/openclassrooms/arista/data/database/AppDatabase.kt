package com.openclassrooms.arista.data.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.openclassrooms.arista.data.dao.ExerciseDtoDao
import com.openclassrooms.arista.data.dao.SleepDtoDao
import com.openclassrooms.arista.data.dao.UserDtoDao
import com.openclassrooms.arista.data.entity.SleepDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneOffset

abstract class AppDatabase : RoomDatabase() {
    abstract fun userDtoDao(): UserDtoDao
    abstract fun sleepDtoDao(): SleepDtoDao
    abstract fun exerciseDtoDao(): ExerciseDtoDao


    private class AppDatabaseCallback(
        private val scope: CoroutineScope
    ) : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.sleepDtoDao(), database.userDtoDao())
                }
            }
        }
    }


    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null


        fun getDatabase(context: Context, coroutineScope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "AristaDB"
                )
                    .addCallback(AppDatabaseCallback(coroutineScope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        suspend fun populateDatabase(sleepDao: SleepDtoDao, userDtoDao: UserDtoDao) {

            sleepDao.insertSleep(
                SleepDto(
                    startTime = LocalDateTime.now().minusDays(1).atZone(ZoneOffset.UTC).toInstant()
                        .toEpochMilli(), duration = 480, quality = 4
                )
            )
            sleepDao.insertSleep(
                SleepDto(
                    startTime = LocalDateTime.now().minusDays(2).atZone(ZoneOffset.UTC).toInstant()
                        .toEpochMilli(), duration = 450, quality = 3
                )
            )
        }
    }
}