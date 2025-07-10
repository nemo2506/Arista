package com.openclassrooms.arista

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
 * Verifies the behavior of the execute method which should return
 * the first user if the list is not empty, or null otherwise.
 */
@RunWith(JUnit4::class)
class GetUserUseCaseTest {

    /** Mock of the user repository used for testing. */
    @Mock
    private lateinit var userRepository: UserRepository

    /** Instance of the use case to be tested. */
    private lateinit var useCase: GetUserUseCase

    /** AutoCloseable resource to manage mocks lifecycle. */
    private lateinit var closeable: AutoCloseable

    /**
     * Setup before each test.
     *
     * Opens mocks and instantiates the use case.
     */
    @Before
    fun setUp() {
        closeable = MockitoAnnotations.openMocks(this)
        useCase = GetUserUseCase(userRepository)
    }

    /**
     * Cleanup after each test.
     *
     * Closes mocks and releases resources.
     */
    @After
    fun tearDown() {
        closeable.close()
        Mockito.framework().clearInlineMocks()
    }

    /**
     * Tests that when the repository is empty,
     * the execute method returns null.
     */
    @Test
    fun quand_le_user_est_vide_le_retour_du_repository_est_null(): Unit = runBlocking {
        Mockito.`when`(userRepository.getFirstUser()).thenReturn(flowOf(null))

        val result = userRepository.getFirstUser().first()

        assertNull(result)
    }
}
