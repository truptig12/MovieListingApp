package com.frogsocial.auth_data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.frogsocial.auth_domain.model.User

@Entity
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long  = 0,
    val firstName: String?,
    val lastName: String?,
    var email: String?,
    var password: String?
) {

    companion object{
        fun fromUser(user : User): UserEntity{
            return  UserEntity(firstName = user.firstName, lastName = user.lastName, email = user.email, password = user.password)
        }
    }


    fun toUser(): User {
        return User(
            id = id!!,
            firstName = firstName,
            lastName = lastName,
            email = email,
            password = password
        )
    }
}