package com.openclassrooms.arista.data.repository

open class UserRepositoryException(message: String, cause: Throwable? = null) : Exception(message, cause)
class MissingUserIdException : UserRepositoryException("User ID is null")
open class SleepRepositoryException(message: String, cause: Throwable? = null) : Exception(message, cause)
class MissingSleepIdException : SleepRepositoryException("Sleep ID is null")
open class ExerciseRepositoryException(message: String, cause: Throwable? = null) : Exception(message, cause)
class MissingExerciseIdException : ExerciseRepositoryException("Exercise ID is null")
