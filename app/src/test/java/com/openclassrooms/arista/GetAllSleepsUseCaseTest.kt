package com.openclassrooms.arista

import com.openclassrooms.arista.data.repository.MissingUserIdException
import com.openclassrooms.arista.data.repository.SleepRepository
import com.openclassrooms.arista.domain.model.Sleep
import com.openclassrooms.arista.domain.model.User
import com.openclassrooms.arista.domain.usecase.GetAllSleepsUseCase
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import com.openclassrooms.arista.domain.usecase.GetUserUseCase
import org.junit.After
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.time.LocalDateTime
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
import kotlin.test.assertFalse

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

    /** Mocked use case to get the current user. */
    @Mock
    private lateinit var userUseCase: GetUserUseCase

    /** The use case under test. */
    private lateinit var useCase: GetAllSleepsUseCase

    /** AutoCloseable resource to manage mock lifecycle. */
    private lateinit var closeable: AutoCloseable

    /** Fixed test date for consistency in tests. */
    private val testDateTime = LocalDateTime.of(2025, 7, 9, 10, 0)

    /**
     * Setup before each test: initialize mocks and use case.
     */
    @Before
    fun setUp() {
        closeable = MockitoAnnotations.openMocks(this)
        useCase = GetAllSleepsUseCase(sleepRepository, userUseCase)
    }

    /**
     * Cleanup mocks after each test.
     */
    @After
    fun tearDown() {
        closeable.close()
        Mockito.framework().clearInlineMocks()
    }

    /**
     * quand_usecase_sexecute_devrait_retourner_seulement_les_sleeps_de_lutilisateur_courant:
     *
     * Tests that the use case returns only sleeps belonging to the current user.
     */
    @Test
    fun quand_usecase_sexecute_devrait_retourner_seulement_les_sleeps_de_lutilisateur_courant() = runBlocking {
        // Arrange
        val testUser = User(
            id = 1L,
            name = "Test User",
            email = "test@example.com",
            password = "pass"
        )
        val sleep1 = Sleep(
            id = 1L,
            startTime = testDateTime,
            duration = 7,
            quality = 1,
            userId = 1L
        )
        val sleep2 = Sleep(
            id = 2L,
            startTime = testDateTime,
            duration = 5,
            quality = 2,
            userId = 2L
        )
        val allSleeps = listOf(sleep1, sleep2)

        Mockito.`when`(userUseCase.execute()).thenReturn(testUser)
        Mockito.`when`(sleepRepository.getAllSleeps()).thenReturn(allSleeps)

        // Act
        val result = useCase.execute()

        // Assert
        assertEquals(1, result.size)
        assertTrue(result.contains(sleep1))
        assertFalse(result.contains(sleep2))
    }

    /**
     * quand_user_est_null_usecase_devrait_renvoyer_MissingUserIdException:
     *
     * Tests that when the current user is null,
     * the use case throws a MissingUserIdException.
     */
    @Test
    fun quand_user_est_null_usecase_devrait_renvoyer_MissingUserIdException(): Unit = runBlocking {
        // Arrange
        Mockito.`when`(userUseCase.execute()).thenReturn(null)
        Mockito.`when`(sleepRepository.getAllSleeps()).thenReturn(emptyList()) // Important to avoid null pointer

        // Act + Assert
        assertFailsWith<MissingUserIdException> {
            useCase.execute()
        }
    }
}
