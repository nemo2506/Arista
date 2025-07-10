package com.openclassrooms.arista

import com.openclassrooms.arista.data.repository.ExerciseRepository
import com.openclassrooms.arista.domain.model.Exercise
import com.openclassrooms.arista.domain.model.ExerciseCategory
import com.openclassrooms.arista.domain.usecase.DeleteExerciseUseCase
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify
import java.time.LocalDateTime

/**
 * Tests unitaires pour la classe [DeleteExerciseUseCase].
 *
 * Vérifie que l'appel à la méthode `execute` déclenche bien
 * la suppression d'un exercice via le repository.
 */
@RunWith(JUnit4::class)
class DeleteExerciseUseCaseTest {

    /** Mock du repository d'exercices utilisé pour les tests. */
    private val exerciseRepository = mock(ExerciseRepository::class.java)

    /** Instance du cas d'utilisation à tester. */
    private lateinit var useCase: DeleteExerciseUseCase

    /** Exercice de test utilisé pour valider la suppression. */
    private val testExercise = Exercise(
        startTime = LocalDateTime.now(),
        duration = 30,
        category = ExerciseCategory.Running,
        intensity = 5,
        userId = 1L
    )

    /**
     * Initialisation avant chaque test.
     *
     * Instancie le use case avec le mock du repository.
     */
    @Before
    fun setUp() {
        useCase = DeleteExerciseUseCase(exerciseRepository)
    }

    /**
     * Teste que l'exécution du use case appelle bien la méthode
     * `deleteExercise` du repository avec l'exercice donné.
     */
    @Test
    fun quand_usecase_sexecute_devrait_appeler_le_repository_pour_supprimer_un_exercise(): Unit = runBlocking {
        // Act
        useCase.execute(this, testExercise)
        // Assert
        verify(exerciseRepository).deleteExercise(testExercise)
    }
}
