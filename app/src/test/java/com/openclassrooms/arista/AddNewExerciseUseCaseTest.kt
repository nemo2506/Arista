package com.openclassrooms.arista

import com.openclassrooms.arista.data.repository.ExerciseRepository
import com.openclassrooms.arista.domain.model.Exercise
import com.openclassrooms.arista.domain.model.ExerciseCategory
import com.openclassrooms.arista.domain.model.User
import com.openclassrooms.arista.domain.usecase.AddNewExerciseUseCase
import com.openclassrooms.arista.domain.usecase.GetUserUseCase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import java.time.LocalDateTime
import kotlin.test.assertFailsWith

/**
 * Unit tests for [AddNewExerciseUseCase].
 */
@RunWith(JUnit4::class)
class AddNewExerciseUseCaseTest {

    @Mock
    private lateinit var exerciseRepository: ExerciseRepository
    @Mock
    private lateinit var getUserUseCase: GetUserUseCase

    // This should NOT be mocked, it’s the class under test
    private lateinit var useCase: AddNewExerciseUseCase

    private lateinit var closeable: AutoCloseable
    private val testDateTime = LocalDateTime.of(2025, 7, 9, 10, 0)
    private val testCategory = ExerciseCategory.Football
    private val testUser = User(
        id = 1L,
        name = "Test",
        email = "test@example.com",
        password = "password"
    )

    /**
     * Initializes mocks and sets up the use case before each test.
     */
    @Before
    fun setUp() {
        closeable = MockitoAnnotations.openMocks(this)
        useCase = AddNewExerciseUseCase(exerciseRepository, getUserUseCase)
    }

    /**
     * Cleans up mock resources after each test.
     */
    @After
    fun tearDown() {
        closeable.close()  // Clean up mocks
        Mockito.framework().clearInlineMocks()
    }

    @Test
    fun quand_usecase_sexecute_avec_un_user_valide_devrait_appeler_le_repository_avec_lexercise_correct() = runBlocking {
        // Arrange
        Mockito.`when`(getUserUseCase.execute()).thenReturn(testUser)
        val input = listOf(testDateTime, 30, testCategory, 3)

        // Act
        useCase.execute(input)

        // Assert
        val expectedExercise = Exercise(
            startTime = testDateTime,
            duration = 30,
            category = testCategory,
            intensity = 3,
            userId = 1L
        )
        verify(exerciseRepository).addExercise(expectedExercise)
    }

    @Test
    fun quand_user_est_null_usecase_devrait_renvoyer_IllegalStateException(): Unit = runBlocking {
        // Arrange
        Mockito.`when`(getUserUseCase.execute()).thenReturn(null)
        val input = listOf(testDateTime, 30, testCategory, 3)

        // Act + Assert
        assertFailsWith<IllegalStateException> {
            useCase.execute(input)
        }
    }
}
