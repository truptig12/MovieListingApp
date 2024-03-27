package com.frogsocial.auth_presentation.screen

import com.frogsocial.auth_domain.model.User

data class RegistrationUIState(
    var firstName :String = "",
    var lastName  :String = "",
    var email  :String = "",
    var password  :String = "",
    var privacyPolicyAccepted :Boolean = false,


    var firstNameError :Boolean = false,
    var lastNameError : Boolean = false,
    var emailError :Boolean = false,
    var passwordError : Boolean = false,
    var privacyPolicyError:Boolean = false


){
    fun toUser(): User {
        return User(
            id = 0,
            firstName = firstName,
            lastName = lastName,
            email = email,
            password = password
        )
    }
}
