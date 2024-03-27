package com.frogsocial.auth_domain

import com.frogsocial.auth_domain.model.User


interface  UserRepository {

    fun addUser(users: User):Long

    fun verifyLoginUser(email:String,password:String): User

    fun getUserDataDetails(id:Long):User
}