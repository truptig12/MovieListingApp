package com.frogsocial.auth_presentation.screen

sealed class LoginSignupUIEvent{

    data class FirstNameChanged(val firstName:String) : LoginSignupUIEvent()
    data class LastNameChanged(val lastName:String) : LoginSignupUIEvent()
    data class EmailChanged(val email:String): LoginSignupUIEvent()
    data class PasswordChanged(val password: String) : LoginSignupUIEvent()

    data object Validate : LoginSignupUIEvent()

    data object RegisterButtonClicked : LoginSignupUIEvent()
    data object LoginButtonClicked : LoginSignupUIEvent()

    data object ExistingUser : LoginSignupUIEvent()
    data object NewUser : LoginSignupUIEvent()

    data object DismissDialog : LoginSignupUIEvent()
    data class ShowDialog(val errorMessage : String ) : LoginSignupUIEvent()
}