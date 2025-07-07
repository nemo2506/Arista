package com.openclassrooms.arista.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.openclassrooms.arista.data.converter.Converters
import com.openclassrooms.arista.data.dao.ExerciseDtoDao
import com.openclassrooms.arista.data.dao.SleepDtoDao
import com.openclassrooms.arista.data.dao.UserDtoDao
import com.openclassrooms.arista.data.entity.ExerciseDto
import com.openclassrooms.arista.data.entity.SleepDto
import com.openclassrooms.arista.data.entity.UserDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneOffset

@Database(
    entities = [UserDto::class, SleepDto::class, ExerciseDto::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
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

            userDtoDao.insertUser(
                UserDto(
                    name = "Joe",
                    email = "joe@example.com",
                    password = "LongPassword"
                )
            )

            sleepDao.insertSleep(
                SleepDto(
                    startTime = LocalDateTime.now(),
                    duration = 480,
                    quality = 4,
                    userId = 1
                )
            )

            sleepDao.insertSleep(
                SleepDto(
                    startTime = LocalDateTime.now(),
                    duration = 450,
                    quality = 3,
                    userId = 1
                )
            )
        }
    }
}