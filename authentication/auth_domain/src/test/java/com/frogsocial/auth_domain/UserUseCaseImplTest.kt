package com.frogsocial.auth_domain

import com.frogsocial.auth_domain.model.User
import com.frogsocial.auth_domain.use_cases.UserUseCaseImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class UserUseCaseImplTest {

    @Mock
    private lateinit var userRepository: UserRepository

    private lateinit var userUseCase: UserUseCaseImpl

    @Before
    fun setUp() {
        userUseCase = UserUseCaseImpl(userRepository)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `addUser invokes userRepository and returns correct ID`() = runTest {
        val user = User(id = 1, firstName = "Test User", lastName = "last name", email = "test@example.com", password = "password123")
        val expectedId = 1L
        `when`(userRepository.addUser(user)).thenReturn(expectedId)

        val resultId = userUseCase.addUser(user)

        assertEquals(expectedId, resultId)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `getUserLoginVerify returns correct user data`() = runTest {
        val email = "email@example.com"
        val password = "password"
        val expectedUser = User(id = 1, firstName = "Test User", lastName = "last name", email = email, password = password)
        `when`(userRepository.verifyLoginUser(email, password)).thenReturn(expectedUser)

        val resultUser = userUseCase.getUserLoginVerify(email, password)

        assertEquals(expectedUser, resultUser)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `getUserData returns correct user details`() = runTest {
        val userId = 1L
        val expectedUser = User(id = 1, firstName = "Test User", lastName = "last name", email = "test@example.com", password = "password123")
        `when`(userRepository.getUserDataDetails(userId)).thenReturn(expectedUser)

        val resultUser = userUseCase.getUserData(userId)

        assertEquals(expectedUser, resultUser)
    }
}
