package com.frogsocial.auth_domain.use_cases

import com.frogsocial.auth_domain.model.User

interface UserUseCase {
    suspend fun addUser(users: User): Long
    suspend fun getUserLoginVerify(email: String, password: String): User
    suspend fun getUserData(id:Long): User
}