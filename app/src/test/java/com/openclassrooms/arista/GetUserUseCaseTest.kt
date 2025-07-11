package com.openclassrooms.arista

import app.cash.turbine.test
import com.openclassrooms.arista.data.repository.UserRepository
import com.openclassrooms.arista.domain.model.User
import com.openclassrooms.arista.domain.usecase.GetUserUseCase
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
import kotlin.test.assertEquals
import org.junit.After
import org.junit.Assert.assertNull

/**
 * Unit tests for the [GetUserUseCase] class.
 *
 * This class verifies the behavior of the [GetUserUseCase.execute] method,
 * which is expected to return the first user from the repository if it exists,
 * or null otherwise.
 */
@RunWith(JUnit4::class)
class GetUserUseCaseTest {

    /** Mocked instance of [UserRepository] used to simulate data source behavior. */
    @Mock
    private lateinit var userRepository: UserRepository

    /** Instance of [GetUserUseCase] under test. */
    private lateinit var getUserUseCase: GetUserUseCase

    /** Handle for closing open mocks after tests. */
    private lateinit var closeable: AutoCloseable

    /** A test user used in assertions. */
    private val actualUser = User(
        id = 1L,
        name = "John Doe",
        email = "johndoe@example.com",
        password = "LongPassword"
    )

    /**
     * Prepares the test environment before each test case.
     *
     * Initializes mock objects and creates an instance of the use case.
     */
    @Before
    fun setUp() {
        closeable = MockitoAnnotations.openMocks(this)
        getUserUseCase = GetUserUseCase(userRepository)
    }

    /**
     * Cleans up the test environment after each test case.
     *
     * Closes open mocks and clears inline mock configurations.
     */
    @After
    fun tearDown() {
        closeable.close()
        Mockito.framework().clearInlineMocks()
    }

    /**
     * Verifies that [execute] emits the expected user when the repository contains a user.
     */
    @Test
    fun execute_doit_emettre_l_utilisateur_du_repository(): Unit = runBlocking {
        // Given
        Mockito.`when`(userRepository.getFirstUser()).thenReturn(flowOf(actualUser))

        // When & Then
        getUserUseCase.execute().test {
            assertEquals(actualUser, awaitItem())
            awaitComplete()
        }
    }

    /**
     * Verifies that [execute] emits null when the repository returns null.
     */
    @Test
    fun execute_doit_emettre_null_si_la_base_est_vide(): Unit = runBlocking {
        // Given
        Mockito.`when`(userRepository.getFirstUser()).thenReturn(flowOf(null))

        // When & Then
        getUserUseCase.execute().test {
            assertNull(awaitItem())
            awaitComplete()
        }
    }
}
