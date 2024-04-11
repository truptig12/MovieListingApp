package com.frogsocial.auth_presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.frogsocial.auth_domain.model.User
import com.frogsocial.auth_domain.use_cases.BiometricAuthenticationUseCase
import com.frogsocial.auth_domain.use_cases.UserUseCase
import com.frogsocial.auth_presentation.screen.LoginSignupUIEvents
import com.frogsocial.core.utils.Resource
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import kotlin.time.ExperimentalTime

@ExperimentalCoroutinesApi
class SignUpTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var usersUseCase: UserUseCase
    @Mock
    private lateinit var biometricUseCase: BiometricAuthenticationUseCase

    private lateinit var viewModel: SignUpViewModelNew

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(TestCoroutineDispatcher())
        usersUseCase = mockk(relaxed = true) // relaxed = true can help avoid some issues with return types
        viewModel = SignUpViewModelNew(usersUseCase, biometricUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // Reset to the original Main dispatcher
    }



    @Test
    fun `validate email and password fields`() = runTest {
        viewModel.onEvent(LoginSignupUIEvents.EmailChanged("test@example.com"))
        viewModel.onEvent(LoginSignupUIEvents.PasswordChanged("password123"))

        viewModel.onEvent(LoginSignupUIEvents.Validate)

        assert(viewModel.allValidationsPassed.value)
    }

    @ExperimentalTime
    @Test
    fun `when RegisterButtonClicked event, insertUserData updates insertUsersDataStatus`() = runBlocking {
        val user = User(id = 1, firstName = "Test User", lastName = "last name", email = "test@example.com", password = "password123")
        val expectedUserId = 1L

        coEvery { usersUseCase.addUser(any()) } returns expectedUserId // Use any() wisely depending on whether the function expects non-null

        viewModel.insertUserData(user)

        viewModel.insertUsersDataStatus.test {
            val item = awaitItem()
            assertEquals(expectedUserId, item.data)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `register user failure`() = runTest {
        val errorMsg = "Registration failed"
        coEvery { usersUseCase.addUser(any()) }.throws(RuntimeException(errorMsg))
        viewModel.onEvent(LoginSignupUIEvents.RegisterButtonClicked)
        viewModel.insertUsersDataStatus.test {
            val item = awaitItem()
            assert(item is Resource.Error)
            assert((item as Resource.Error).message == errorMsg)


            cancelAndIgnoreRemainingEvents()
        }


    }

    @Test
    fun `biometric authentication available and succeeds`() = runTest {
        Mockito.`when`(biometricUseCase.isBiometricAvailable()).thenReturn(true)

        viewModel.authenticateUser()

        assert(viewModel.biometricState.value == SignUpViewModelNew.BiometricState.Authenticating)
        // Simulate success callback here
        viewModel.updateBiometricState(SignUpViewModelNew.BiometricState.Success)

        assert(viewModel.biometricState.value == SignUpViewModelNew.BiometricState.Success)
    }

    @Test
    fun `biometric authentication unavailable`() = runTest {
        Mockito.`when`(biometricUseCase.isBiometricAvailable()).thenReturn(false)

        viewModel.authenticateUser()

        assert(viewModel.biometricState.value == SignUpViewModelNew.BiometricState.Unavailable)
    }

    @Test
    fun `UI state updates for existing user`() = runTest {
        viewModel.onEvent(LoginSignupUIEvents.ExistingUser)

        val uiState = viewModel.uiState.value
        assert(uiState.isExistingUser)
        assert(!uiState.showDialog)
        assert(uiState.errorMessage.isEmpty())
    }

    @Test
    fun `UI state updates for new user`() = runTest {
        viewModel.onEvent(LoginSignupUIEvents.NewUser)

        val uiState = viewModel.uiState.value
        assert(!uiState.isExistingUser)
    }
}