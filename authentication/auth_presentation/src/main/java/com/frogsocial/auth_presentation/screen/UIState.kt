package com.frogsocial.auth_presentation.screen

import com.frogsocial.auth_domain.model.User

data class UIState(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val password: String = "",
    val isExistingUser: Boolean = true,
    val showDialog: Boolean = false,
    val errorMessage: String = "",
    val navigationCommand: NavigationCommand? = null,

    var firstNameError :Boolean = false,
    var lastNameError : Boolean = false,
    var emailError :Boolean = false,
    var passwordError : Boolean = false,
    var privacyPolicyError:Boolean = false
) {
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

sealed class LoginSignupUIEvents {
    data class FirstNameChanged(val name: String) : LoginSignupUIEvents()
    data class LastNameChanged(val lastName: String) : LoginSignupUIEvents()
    data class EmailChanged(val email: String) : LoginSignupUIEvents()
    data class PasswordChanged(val password: String) : LoginSignupUIEvents()
    object Validate : LoginSignupUIEvents()
    object LoginButtonClicked : LoginSignupUIEvents()
    object RegisterButtonClicked : LoginSignupUIEvents()
    data class ShowDialog(val message: String) : LoginSignupUIEvents()
    object DismissDialog : LoginSignupUIEvents()
    object NewUser : LoginSignupUIEvents()
    object ExistingUser : LoginSignupUIEvents()
    object BiometricAuthRequested: LoginSignupUIEvents()
    object LoginAfterRegister: LoginSignupUIEvents()
}

sealed class NavigationCommand {
    object NavigateToHome : NavigationCommand()
}