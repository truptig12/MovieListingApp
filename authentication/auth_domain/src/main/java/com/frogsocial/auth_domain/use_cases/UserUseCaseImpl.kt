package com.frogsocial.auth_domain.use_cases

import com.frogsocial.auth_domain.UserRepository
import com.frogsocial.auth_domain.model.User
import javax.inject.Inject

class UserUseCaseImpl @Inject constructor(private var userRepository: UserRepository):UserUseCase{
    override suspend fun addUser(users: User): Long {
        val id= userRepository.addUser(users)
        return id
    }

    override suspend fun getUserLoginVerify(email:String, password:String): User {
        return userRepository.verifyLoginUser(email, password)
    }

    override suspend fun getUserData(id: Long): User {
        return userRepository.getUserDataDetails(id)
    }
}