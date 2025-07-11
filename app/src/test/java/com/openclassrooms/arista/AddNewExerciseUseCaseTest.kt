package com.openclassrooms.arista

import com.openclassrooms.arista.data.repository.ExerciseRepository
import com.openclassrooms.arista.domain.model.Exercise
import com.openclassrooms.arista.domain.model.ExerciseCategory
import com.openclassrooms.arista.domain.usecase.AddNewExerciseUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.Mock
import java.time.LocalDateTime
import kotlin.test.assertEquals
import org.junit.After
import org.junit.Assert.assertTrue

/**
 * Unit tests for [AddNewExerciseUseCase].
 */
@RunWith(JUnit4::class)
class AddNewExerciseUseCaseTest {

    /** Mocked repository to retrieve exercise. */
    @Mock
    private lateinit var exerciseRepository: ExerciseRepository

    private lateinit var addNewExerciseUseCase: AddNewExerciseUseCase

    /** AutoCloseable resource to manage mock lifecycle. */
    private lateinit var closeable: AutoCloseable

    @Before
    fun setUp() {
        closeable = MockitoAnnotations.openMocks(this)
        addNewExerciseUseCase = AddNewExerciseUseCase(exerciseRepository)
    }

    /**
     * Cleanup mocks after each test.
     */
    @After
    fun tearDown() {
        closeable.close()
        Mockito.framework().clearInlineMocks()
    }

    @Test
    fun lorsque_le_repository_reussit_et_le_useCase_doit_renvoyer_un_resultat_reussi() =
        runBlocking {
            // Arrange
            val exercise = Exercise(
                startTime = LocalDateTime.now(),
                duration = 30,
                category = ExerciseCategory.Running,
                intensity = 5,
                userId = 1L
            )
            Mockito.`when`(exerciseRepository.addExercise(exercise))
                .thenReturn(flowOf(Result.success(Unit)))
            // Act
            val result = addNewExerciseUseCase.execute(exercise).first()

            // Assert
            assertTrue(result.isSuccess)
        }

    @Test
    fun lorsque_le_repository_echoue_et_le_useCase_doit_renvoyer_un_echec_de_resultat() =
        runBlocking {
            // Arrange
            val exercise = Exercise(
                startTime = LocalDateTime.now(),
                duration = 45,
                category = ExerciseCategory.Riding,
                intensity = 7,
                userId = 1L
            )
            val exception = RuntimeException("Insert failed")
            Mockito.`when`(exerciseRepository.addExercise(exercise))
                .thenReturn(flowOf(Result.failure(exception)))
            // Act
            val result = addNewExerciseUseCase.execute(exercise).first()

            // Assert
            assertTrue(result.isFailure)
            assertEquals("Insert failed", result.exceptionOrNull()?.message)
        }
}

