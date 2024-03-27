package com.frogsocial.auth_data.repository

import com.frogsocial.auth_data.local.UserDao
import com.frogsocial.auth_data.local.entity.UserEntity
import com.frogsocial.auth_domain.UserRepository
import com.frogsocial.auth_domain.model.User
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private var usersDao: UserDao
    ) : UserRepository {

    override fun addUser(user: User): Long {
        return usersDao.insertUser(UserEntity.fromUser(user))
    }

    override fun verifyLoginUser(email: String, password: String): User {
        val userEntity = usersDao.readLoginData(email = email, password = password)
        return userEntity.toUser()
    }

    override fun getUserDataDetails(id: Long): User {
        val userEntity = usersDao.getUserDataDetails(id)
        return userEntity.toUser()
    }
}
