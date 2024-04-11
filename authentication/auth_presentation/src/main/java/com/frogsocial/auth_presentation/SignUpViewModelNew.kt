package com.frogsocial.auth_presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frogsocial.auth_domain.model.User
import com.frogsocial.auth_domain.use_cases.BiometricAuthenticationUseCase
import com.frogsocial.auth_domain.use_cases.UserUseCase
import com.frogsocial.auth_presentation.components.ValidationResultN
import com.frogsocial.auth_presentation.components.ValidatorNew
import com.frogsocial.auth_presentation.screen.LoginSignupUIEvent
import com.frogsocial.auth_presentation.screen.LoginSignupUIEvents
import com.frogsocial.auth_presentation.screen.NavigationCommand
import com.frogsocial.auth_presentation.screen.UIState
import com.frogsocial.auth_presentation.screen.ValidationResult
import com.frogsocial.auth_presentation.screen.Validator
import com.frogsocial.core.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModelNew @Inject constructor(
    private var usersUseCase: UserUseCase,
    private val biometricUseCase: BiometricAuthenticationUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(UIState())
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()

    var allValidationsPassed = mutableStateOf(false)

    private val _insertUsersDataStatus = MutableStateFlow<Resource<Long>>(Resource.Loading(null))
    val insertUsersDataStatus: StateFlow<Resource<Long>> = _insertUsersDataStatus

    private val _usersAuthStatus = MutableStateFlow<Resource<User>>(Resource.Loading(null))
    val usersAuthStatus: StateFlow<Resource<User>> = _usersAuthStatus

    private val _biometricState = mutableStateOf<BiometricState>(BiometricState.Initial)
    val biometricState: State<BiometricState> = _biometricState
    fun onEvent(event: LoginSignupUIEvents) {
        when (event) {
            is LoginSignupUIEvents.FirstNameChanged -> _uiState.value = _uiState.value.copy(firstName = event.name)
            is LoginSignupUIEvents.LastNameChanged -> {
                _uiState.value = _uiState.value.copy(lastName = event.lastName)
            }
            is LoginSignupUIEvents.EmailChanged -> {
                _uiState.value = _uiState.value.copy(email = event.email)
            }
            is LoginSignupUIEvents.PasswordChanged -> {
                _uiState.value = _uiState.value.copy(password = event.password)
            }
            is LoginSignupUIEvents.Validate -> {
                validateDataWithRules(!_uiState.value.isExistingUser)
                if (allValidationsPassed.value) {
                    val nextEvent = if (uiState.value.isExistingUser) LoginSignupUIEvents.LoginButtonClicked else LoginSignupUIEvents.RegisterButtonClicked
                    onEvent(nextEvent)
                } else {
                    onEvent(LoginSignupUIEvents.ShowDialog("Please check the data and try again"))
                }
            }
            is LoginSignupUIEvents.RegisterButtonClicked -> {
                insertUserData(_uiState.value.toUser())
            }
            is LoginSignupUIEvents.ShowDialog -> {
                _uiState.value = _uiState.value.copy(showDialog = true, errorMessage = event.message)
            }
            is LoginSignupUIEvents.DismissDialog -> {
                _uiState.value = _uiState.value.copy(showDialog = false, errorMessage = "")
            }
            is LoginSignupUIEvents.NewUser -> {
                _uiState.value = _uiState.value.copy(isExistingUser = false)
            }
            is LoginSignupUIEvents.ExistingUser -> {
                _uiState.value = _uiState.value.copy(isExistingUser = true)
            }

            is LoginSignupUIEvents.LoginButtonClicked -> {
                verifyUser(_uiState.value.email, _uiState.value.password)
            }
            is LoginSignupUIEvents.BiometricAuthRequested ->{
                authenticateUser()
            }
            is LoginSignupUIEvents.LoginAfterRegister ->{
                _uiState.value = _uiState.value.copy(firstName = "", lastName = "", email = "", password = "", isExistingUser = true)
            }
            else -> {}
        }
    }

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
                _usersAuthStatus.value = Resource.Error("Login Failed", null)
            }
        }
    }

    private fun validateDataWithRules(isSignUp: Boolean) {
        // Initial validation results assume success
        var fNameResult = ValidationResultN(true)
        var lNameResult = ValidationResultN(true)
        var privacyPolicyResult = ValidationResultN(true)

        if (isSignUp) {
            fNameResult = ValidatorNew.validateFirstName(_uiState.value.firstName)
            lNameResult = ValidatorNew.validateLastName(_uiState.value.lastName)
          //  privacyPolicyResult = Validator.validatePrivacyPolicyAcceptance(_uiState.value.privacyPolicyAccepted)
        }

        val emailResult = ValidatorNew.validateEmail(_uiState.value.email)
        val passwordResult = ValidatorNew.validatePassword(_uiState.value.password)

        // Update UI state with validation results
        _uiState.value = _uiState.value.copy(
            firstNameError = !fNameResult.status,
            lastNameError = !lNameResult.status,
            emailError = !emailResult.status,
            passwordError = !passwordResult.status,
            //privacyPolicyError = !privacyPolicyResult.status
        )

        // Aggregate validation status
        allValidationsPassed.value = listOf(fNameResult, lNameResult, emailResult, passwordResult, privacyPolicyResult).all { it.status }
    }



    fun authenticateUser() {
        if (biometricUseCase.isBiometricAvailable()) {
            _biometricState.value = BiometricState.Authenticating
            // Trigger biometric prompt and update state based on result
        } else {
            _biometricState.value = BiometricState.Unavailable
        }
    }

    fun updateBiometricState(biometricState: BiometricState){
        when(biometricState){

            BiometricState.Success -> {
                _biometricState.value = BiometricState.Success
            }

            BiometricState.Authenticating ->{
                _biometricState.value = BiometricState.Authenticating
            }
            BiometricState.Error -> {
                _biometricState.value = BiometricState.Error
            }
            BiometricState.Initial -> {
                _biometricState.value = BiometricState.Initial
            }
            BiometricState.Unavailable -> {
                _biometricState.value = BiometricState.Unavailable
            }
        }
    }
    sealed class BiometricState {
        object Initial : BiometricState()
        object Authenticating : BiometricState()
        object Success : BiometricState()
        object Error : BiometricState()
        object Unavailable : BiometricState()
    }
}