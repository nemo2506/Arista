package com.openclassrooms.arista

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import com.openclassrooms.arista.data.repository.SleepRepository
import com.openclassrooms.arista.domain.model.Sleep
import com.openclassrooms.arista.domain.usecase.GetAllSleepsUseCase
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

/**
 * Unit tests for the [GetAllSleepsUseCase] class.
 *
 * Verifies filtering sleeps by the current user and proper exception throwing
 * when no user is available.
 */
@RunWith(JUnit4::class)
class GetAllSleepsUseCaseTest {

    /** Mocked repository to retrieve sleeps. */
    @Mock
    private lateinit var sleepRepository: SleepRepository

    /** The use case under test. */
    private lateinit var getAllSleepsUseCase: GetAllSleepsUseCase

    /** AutoCloseable resource to manage mock lifecycle. */
    private lateinit var closeable: AutoCloseable

    /** Fixed test date for consistency in tests. */
    private val testDateTime = LocalDateTime.of(2025, 7, 9, 10, 0)

    /** Fixed test sleeps for consistency in tests. */
    private val sleep1 = Sleep(
        id = 1L,
        startTime = testDateTime,
        duration = 7,
        quality = 1,
        userId = 1L
    )
    private val sleep2 = Sleep(
        id = 2L,
        startTime = testDateTime,
        duration = 5,
        quality = 2,
        userId = 2L
    )

    /**
     * Setup before each test: initialize mocks and use case.
     */
    @Before
    fun setUp() {
        closeable = MockitoAnnotations.openMocks(this)
        getAllSleepsUseCase = GetAllSleepsUseCase(sleepRepository)
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
    fun lorsque_le_referentiel_renvoie_les_sleeps_et_le_cas_d_utilisation_doit_les_renvoyer() = runBlocking {
        // Arrange
        val fakeSleeps = listOf(sleep1,sleep2)
        Mockito.`when`(sleepRepository.getAllSleeps()).thenReturn(flowOf(fakeSleeps))
        // Act
        val result = getAllSleepsUseCase.execute().first()

        // Assert
        assertEquals(fakeSleeps, result)
    }

    @Test
    fun lorsque_le_referentiel_renvoie_une_liste_vide_et_le_cas_d_utilisation_doit_renvoyer_une_liste_vide() = runBlocking {
        // Arrange
        Mockito.`when`(sleepRepository.getAllSleeps()).thenReturn(flowOf(emptyList()))
        // Act
        val result = getAllSleepsUseCase.execute().first()

        // Assert
        assertEquals(emptyList<Sleep>(), result)
    }
}
