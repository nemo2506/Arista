package com.openclassrooms.arista

import com.openclassrooms.arista.data.repository.ExerciseRepository
import com.openclassrooms.arista.data.repository.UserRepository
import com.openclassrooms.arista.domain.model.Exercise
import com.openclassrooms.arista.domain.model.ExerciseCategory
import com.openclassrooms.arista.domain.model.User
import com.openclassrooms.arista.domain.usecase.GetAllExercisesUseCase
import kotlinx.coroutines.runBlocking
import org.junit.runners.JUnit4
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.time.LocalDateTime

/**
 * Unit tests for the [GetAllExercisesUseCase] class.
 * Verifies the logic that filters exercises by the first user's ID.
 */
@RunWith(JUnit4::class)
class GetAllExercisesUseCaseTest {

    @Mock
    private lateinit var exerciseRepository: ExerciseRepository

    @Mock
    private lateinit var userRepository: UserRepository

    private lateinit var getAllExercisesUseCase: GetAllExercisesUseCase
    private lateinit var closeable: AutoCloseable

    /**
     * Initializes mocks and sets up the use case before each test.
     */
    @Before
    fun setUp() {
        closeable = MockitoAnnotations.openMocks(this)
        getAllExercisesUseCase = GetAllExercisesUseCase(exerciseRepository, userRepository)
    }

    /**
     * Cleans up mock resources after each test.
     */
    @After
    fun tearDown() {
        closeable.close()  // Clean up mocks
        Mockito.framework().clearInlineMocks()
    }

    /**
     * Tests that the use case returns the expected list of exercises
     * when the repository contains exercises for the first user.
     */
    @Test
    fun when_Repository_Returns_Exercises_UseCase_Should_Return_Them(): Unit = runBlocking {
        // Arrange
        val testUser =
            User(id = 1L, name = "Test", email = "test@example.com", password = "password")

        Mockito.`when`(userRepository.getAllUsers()).thenReturn(listOf(testUser))

        val fakeExercises = listOf(
            Exercise(
                startTime = LocalDateTime.now(),
                duration = 30,
                category = ExerciseCategory.Running,
                intensity = 5,
                userId = 1
            ),
            Exercise(
                startTime = LocalDateTime.now().plusHours(1),
                duration = 45,
                category = ExerciseCategory.Riding,
                intensity = 7,
                userId = 1
            )
        )
        Mockito.`when`(exerciseRepository.getAllExercises()).thenReturn(fakeExercises)

        // Act
        val result = getAllExercisesUseCase.execute()

        // Assert
        assertEquals(fakeExercises, result)
    }

    /**
     * Tests that the use case returns an empty list when the exercise repository is empty.
     */
    @Test
    fun when_Repository_Returns_Empty_List_UseCase_Should_Return_EmptyList(): Unit = runBlocking {
        // Arrange
        val testUser = User(id = 1L, name = "Test", email = "test@example.com", password = "password")
        Mockito.`when`(userRepository.getAllUsers()).thenReturn(listOf(testUser))
        Mockito.`when`(exerciseRepository.getAllExercises()).thenReturn(emptyList())

        // Act
        val result = getAllExercisesUseCase.execute()

        // Assert
        assertTrue(result.isEmpty())
    }
}
