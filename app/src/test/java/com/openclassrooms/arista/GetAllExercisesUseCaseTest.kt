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
 * <p>
 * Verifies that the use case correctly filters and returns exercises
 * that belong to the first user retrieved from the [UserRepository].
 * </p>
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
     * Setup method executed before each test.
     * Initializes Mockito mocks and creates the use case instance.
     */
    @Before
    fun setUp() {
        closeable = MockitoAnnotations.openMocks(this)
        getAllExercisesUseCase = GetAllExercisesUseCase(exerciseRepository, userRepository)
    }

    /**
     * Tear down method executed after each test.
     * Cleans up Mockito mocks to avoid memory leaks.
     */
    @After
    fun tearDown() {
        closeable.close()  // Clean up mocks
        Mockito.framework().clearInlineMocks()
    }

    /**
     * Tests that the use case returns all exercises associated with the first user
     * when the repositories contain the expected data.
     */
    @Test
    fun quand_le_repository_retourne_des_exercises_usecase_devrait_retourner_les_exercises(): Unit = runBlocking {
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
     * Tests that the use case returns an empty list
     * when the exercise repository contains no exercises.
     */
    @Test
    fun quand_le_repository_retourne_une_liste_vide_usecase_devrait_retourner_une_liste_vide(): Unit = runBlocking {
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
