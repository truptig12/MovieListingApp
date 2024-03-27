package com.frogsocial.auth_data

import com.frogsocial.auth_data.local.UserDao
import com.frogsocial.auth_data.local.entity.UserEntity
import com.frogsocial.auth_data.repository.UserRepositoryImpl
import com.frogsocial.auth_domain.model.User
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class UserRepositoryImplTest {

    @Mock
    private lateinit var userDao: UserDao

    private lateinit var userRepository: UserRepositoryImpl

    @Before
    fun setUp() {
        userRepository = UserRepositoryImpl(userDao)
    }

    @Test
    fun `addUser calls insertUser on UserDao`() {
        val user = User(id = 1, firstName = "Test User", lastName = "last name", email = "test@example.com", password = "password123")
        val userEntity = UserEntity.fromUser(user)
        `when`(userDao.insertUser(userEntity)).thenReturn(1L)

        val result = userRepository.addUser(user)

        verify(userDao).insertUser(userEntity)
        assertEquals(1L, result)
    }

    @Test
    fun `verifyLoginUser calls readLoginData on UserDao and returns User`() {
        val email = "test@example.com"
        val password = "password123"
        val userEntity = UserEntity(1, "Test User", lastName = "last name",email= email, password = password)
        `when`(userDao.readLoginData(email, password)).thenReturn(userEntity)

        val result = userRepository.verifyLoginUser(email, password)

        verify(userDao).readLoginData(email, password)
        assertEquals(userEntity.toUser(), result)
    }

    @Test
    fun `getUserDataDetails calls getUserDataDetails on UserDao and returns User`() {
        val id = 1L
        val userEntity = UserEntity(1, "Test User", lastName = "last name",email= "test@example.com", password = "password123")
        `when`(userDao.getUserDataDetails(id)).thenReturn(userEntity)

        val result = userRepository.getUserDataDetails(id)

        verify(userDao).getUserDataDetails(id)
        assertEquals(userEntity.toUser(), result)
    }
}
