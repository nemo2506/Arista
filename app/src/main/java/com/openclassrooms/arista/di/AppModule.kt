package com.openclassrooms.arista.di

import android.content.Context
import com.openclassrooms.arista.data.dao.ExerciseDtoDao
import com.openclassrooms.arista.data.dao.SleepDtoDao
import com.openclassrooms.arista.data.dao.UserDtoDao
import com.openclassrooms.arista.data.database.AppDatabase
import com.openclassrooms.arista.data.repository.ExerciseRepository
import com.openclassrooms.arista.data.repository.SleepRepository
import com.openclassrooms.arista.data.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
//    fun provideCoroutineScope(): CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    fun provideCoroutineScope(): CoroutineScope = CoroutineScope(SupervisorJob())

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
        coroutineScope: CoroutineScope
    ): AppDatabase {
        return try {
            AppDatabase.getDatabase(context, coroutineScope)
        } catch (e: Exception) {
            throw RuntimeException("Failed to provide AppDatabase", e)
        }
    }

    @Provides
    fun provideUserDao(appDatabase: AppDatabase): UserDtoDao {
        return try {
            appDatabase.userDtoDao()
        } catch (e: Exception) {
            throw RuntimeException("Failed to provide UserDtoDao", e)
        }
    }

    @Provides
    fun provideSleepDao(appDatabase: AppDatabase): SleepDtoDao {
        return try {
            appDatabase.sleepDtoDao()
        } catch (e: Exception) {
            throw RuntimeException("Failed to provide SleepDtoDao", e)
        }
    }

    @Provides
    fun provideExerciseDao(appDatabase: AppDatabase): ExerciseDtoDao {
        return try {
            appDatabase.exerciseDtoDao()
        } catch (e: Exception) {
            throw RuntimeException("Failed to provide ExerciseDtoDao", e)
        }
    }

    @Provides
    @Singleton
    fun provideUserRepository(userDtoDao: UserDtoDao): UserRepository {
        return try {
            UserRepository(userDtoDao)
        } catch (e: Exception) {
            throw RuntimeException("Failed to provide UserRepository", e)
        }
    }

    @Provides
    @Singleton
    fun provideSleepRepository(sleepDtoDao: SleepDtoDao): SleepRepository {
        return try {
            SleepRepository(sleepDtoDao)
        } catch (e: Exception) {
            throw RuntimeException("Failed to provide SleepRepository", e)
        }
    }

    @Provides
    @Singleton
    fun provideExerciseRepository(exerciseDtoDao: ExerciseDtoDao): ExerciseRepository {
        return try {
            ExerciseRepository(exerciseDtoDao)
        } catch (e: Exception) {
            throw RuntimeException("Failed to provide ExerciseRepository", e)
        }
    }

}