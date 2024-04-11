package com.frogsocial.auth_presentation.screen

import android.content.Context
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.frogsocial.auth_presentation.R
import com.frogsocial.auth_presentation.SignUpViewModelNew
import com.frogsocial.auth_presentation.components.CustomTextField
import com.frogsocial.auth_presentation.components.SimpleDialog
import com.frogsocial.auth_presentation.screen.LoginSignupUIEvents.ShowDialog
import com.frogsocial.core.utils.Resource
import kotlin.reflect.KFunction1

@Composable
fun SignInScreen(viewModel: SignUpViewModelNew, navController: NavHostController) {

    val uiState by viewModel.uiState.collectAsState()
    val insertUsersDataStatus by viewModel.insertUsersDataStatus.collectAsState()
    val usersAuthStatus by viewModel.usersAuthStatus.collectAsState()
    val biometricState by viewModel.biometricState

    val context = LocalContext.current

    SignUpForm(uiState, viewModel::onEvent)

    if (uiState.showDialog) {
        SimpleDialog(message = uiState.errorMessage) { viewModel.onEvent(LoginSignupUIEvents.DismissDialog) }
    }

    LaunchedEffect(insertUsersDataStatus) {
        when (insertUsersDataStatus) {
            is Resource.Success -> {
              viewModel.onEvent(LoginSignupUIEvents.LoginAfterRegister)
            }

            is Resource.Error -> {
                viewModel.onEvent(ShowDialog(insertUsersDataStatus.message ?: "An error occurred"))
            }

            is Resource.Loading -> { /* Optionally handle loading state */
            }
        }
    }

    LaunchedEffect(usersAuthStatus) {
        when (usersAuthStatus) {
            is Resource.Success -> {
                //viewModel.onEvent(LoginSignupUIEvents.BiometricAuthRequested)
                navigate(navController, "home")
            }

            is Resource.Error -> {
                viewModel.onEvent(ShowDialog(usersAuthStatus.message ?: "An error occurred"))
            }

            is Resource.Loading -> { /* Optionally handle loading state */
            }
        }
    }

    LaunchedEffect(biometricState) {
        when (biometricState) {
            SignUpViewModelNew.BiometricState.Authenticating -> showBiometricPromptN(
                context,
                viewModel
            )

            SignUpViewModelNew.BiometricState.Success -> {
                navigate(navController, "home")

            }

            else -> {
               // navigate(navController, "home")
                // Show Toast message
            }
        }
    }

}

@Composable
fun SignUpForm(uiState: UIState, onEvent: KFunction1<LoginSignupUIEvents, Unit>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color(0xFFF9F9F9)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!uiState.isExistingUser) {
            CustomTextField(
                value = uiState.firstName,
                onValueChange = { onEvent(LoginSignupUIEvents.FirstNameChanged(it)) },
                label = "First Name",
                keyboardType = KeyboardType.Text
            )
            CustomTextField(
                value = uiState.lastName,
                onValueChange = { onEvent(LoginSignupUIEvents.LastNameChanged(it)) },
                label = "Last Name",
                keyboardType = KeyboardType.Text
            )
        }
        CustomTextField(
            value = uiState.email,
            onValueChange = { onEvent(LoginSignupUIEvents.EmailChanged(it)) },
            label = "Email",
            keyboardType = KeyboardType.Email
        )
        CustomTextField(
            value = uiState.password,
            onValueChange = { onEvent(LoginSignupUIEvents.PasswordChanged(it)) },
            label = "Password",
            keyboardType = KeyboardType.Password,
            visualTransformation = PasswordVisualTransformation()
        )

        // Buttons and other UI elements
        Button(
            onClick = {
                onEvent(LoginSignupUIEvents.Validate)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = if (uiState.isExistingUser) "Login" else "Sign Up")
        }

        // Switch Mode Text
        Text(
            text = if (uiState.isExistingUser) "New here? Sign up" else "Already have an account? Log in",
            modifier = Modifier
                .clickable { onEvent(if (uiState.isExistingUser) LoginSignupUIEvents.NewUser else LoginSignupUIEvents.ExistingUser) }
                .padding(top = 8.dp),
            color = MaterialTheme.colors.secondary
        )

        // Optionally, add biometric authentication for existing users
        if (uiState.isExistingUser) {
            Image(
                painter = painterResource(R.drawable.fingerprint),
                contentDescription = "Biometric Logo",
                modifier = Modifier.size(128.dp)
                    .wrapContentWidth(Alignment.CenterHorizontally)
                    .clickable {
                        onEvent(LoginSignupUIEvents.BiometricAuthRequested)
                    }
            )
        }

    }
}

fun navigate(navController: NavController, route: String) {
    navController.navigate(route) {
        popUpTo(navController.graph.startDestinationId) {
            inclusive = true
        }
        launchSingleTop = true
    }
}
fun showBiometricPromptN(context: Context, viewModel: SignUpViewModelNew) {
    val executor = ContextCompat.getMainExecutor(context)
    val biometricPrompt = BiometricPrompt(
        context as FragmentActivity,
        executor,
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                viewModel.updateBiometricState(SignUpViewModelNew.BiometricState.Error)
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                viewModel.updateBiometricState(SignUpViewModelNew.BiometricState.Success)
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                viewModel.updateBiometricState(SignUpViewModelNew.BiometricState.Error)
            }
        })

    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle("Biometric Authentication")
        .setDescription("Log in using your biometric credential")
        .setNegativeButtonText("Use account password")
        .build()

    biometricPrompt.authenticate(promptInfo)
}