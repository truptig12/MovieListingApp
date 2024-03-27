package com.frogsocial.auth_presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.frogsocial.auth_domain.model.User
import com.frogsocial.auth_domain.use_cases.UserUseCase
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.time.ExperimentalTime

@ExperimentalCoroutinesApi
class SignUpViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: SignUpViewModel
    private val userUseCase: UserUseCase = mockk()

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined) // Use Unconfined dispatcher for testing
        viewModel = SignUpViewModel(userUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @ExperimentalTime
    @Test
    fun `when RegisterButtonClicked event, insertUserData updates insertUsersDataStatus`() = runTest {
        val user = User(id = 1, firstName = "Test User", lastName = "last name", email = "test@example.com", password = "password123")
        val expectedUserId = 1L
        coEvery { userUseCase.addUser(any()) } returns expectedUserId

        viewModel.insertUserData(user)

        viewModel.insertUsersDataStatus.test {
            val item = awaitItem()
            assertEquals(expectedUserId, item.data)
            cancelAndConsumeRemainingEvents()
        }
    }

}
