
package com.frogsocial.auth_presentation
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frogsocial.auth_domain.model.User
import com.frogsocial.auth_domain.use_cases.UserUseCase
import com.frogsocial.auth_presentation.screen.LoginSignupUIEvent
import com.frogsocial.auth_presentation.screen.RegistrationUIState
import com.frogsocial.auth_presentation.screen.ValidationResult
import com.frogsocial.auth_presentation.screen.Validator
import com.frogsocial.core.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(private var usersUseCase: UserUseCase) : ViewModel() {

    var registrationUIState = mutableStateOf(RegistrationUIState())

    var allValidationsPassed = mutableStateOf(false)

    var showDialog = mutableStateOf(false)

    val isExistingUser = mutableStateOf(true)
    val errorMessage = mutableStateOf("Something is wrong")


    fun onEvent(event: LoginSignupUIEvent) {
        when (event) {
            is LoginSignupUIEvent.FirstNameChanged -> {
                registrationUIState.value = registrationUIState.value.copy(
                    firstName = event.firstName
                )
            }

            is LoginSignupUIEvent.LastNameChanged -> {
                registrationUIState.value = registrationUIState.value.copy(
                    lastName = event.lastName
                )
            }

            is LoginSignupUIEvent.EmailChanged -> {
                registrationUIState.value = registrationUIState.value.copy(
                    email = event.email
                )
            }


            is LoginSignupUIEvent.PasswordChanged -> {
                registrationUIState.value = registrationUIState.value.copy(
                    password = event.password
                )
            }

            is LoginSignupUIEvent.RegisterButtonClicked -> {
                insertUserData(registrationUIState.value.toUser())
            }

            LoginSignupUIEvent.LoginButtonClicked -> {
                verifyUser(registrationUIState.value.email, registrationUIState.value.password)
            }
            LoginSignupUIEvent.Validate -> {
                validateDataWithRules(!isExistingUser.value)
            }

            LoginSignupUIEvent.ExistingUser -> {
                isExistingUser.value = true
            }
            LoginSignupUIEvent.NewUser -> {
                isExistingUser.value = false
            }

            LoginSignupUIEvent.DismissDialog -> {
                showDialog.value = false
            }

            is LoginSignupUIEvent.ShowDialog -> {
                showDialog.value= true
//                errorMessage.value = event.errorMessage
            }

            else -> {}
        }
    }

    private fun validateDataWithRules(isSignUp : Boolean) {
        var fNameResult = ValidationResult(true)
        var lNameResult = ValidationResult(true)
        if (isSignUp){
            fNameResult = Validator.validateFirstName(
                fName = registrationUIState.value.firstName
            )

            lNameResult = Validator.validateLastName(
                lName = registrationUIState.value.lastName
            )
        }

        val emailResult = Validator.validateEmail(
            email = registrationUIState.value.email
        )


        val passwordResult = Validator.validatePassword(
            password = registrationUIState.value.password
        )


        registrationUIState.value = registrationUIState.value.copy(
            firstNameError = fNameResult.status,
            lastNameError = lNameResult.status,
            emailError = emailResult.status,
            passwordError = passwordResult.status,
        )



        allValidationsPassed.value = fNameResult.status && lNameResult.status &&
                emailResult.status && passwordResult.status

    }

    private val _insertUsersDataStatus = MutableStateFlow<Resource<Long>>(Resource.Loading(null))
    val insertUsersDataStatus: StateFlow<Resource<Long>> = _insertUsersDataStatus

    private val _usersAuthStatus = MutableStateFlow<Resource<User>>(Resource.Loading(null))
    val usersAuthStatus: StateFlow<Resource<User>> = _usersAuthStatus

    fun insertUserData(users: User) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data =usersUseCase.addUser(users)
                if (data <0){
                    Resource.Error("Signup Failed", null)
                }else{
                    _insertUsersDataStatus.value = Resource.Success(data)
                }
            } catch (exception: Exception) {
                _insertUsersDataStatus.value = Resource.Error(exception.message!!, null)
            }
        }
    }

    fun verifyUser(email : String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data =usersUseCase.getUserLoginVerify(email, password)
                if (data != null){
                    _usersAuthStatus.value = Resource.Success(data)
                }else{
                    _usersAuthStatus.value = Resource.Error("Login Failed", null)
                }
            } catch (exception: Exception) {
                _usersAuthStatus.value = Resource.Error(exception.message!!, null)
            }
        }
    }

}