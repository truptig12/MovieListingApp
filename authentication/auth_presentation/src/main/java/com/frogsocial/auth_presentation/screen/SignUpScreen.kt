package com.frogsocial.auth_presentation.screen

import android.content.Context
import androidx.biometric.BiometricPrompt
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
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
import androidx.navigation.NavHostController
import com.frogsocial.auth_domain.model.User
import com.frogsocial.auth_presentation.R
import com.frogsocial.auth_presentation.SignUpViewModel
import com.frogsocial.auth_presentation.components.CustomTextField
import com.frogsocial.auth_presentation.components.SimpleDialog
import com.frogsocial.core.utils.Resource

@Composable
fun SignUpScreen(viewModel: SignUpViewModel, navController: NavHostController) {

    val insertUsersDataStatus by viewModel.insertUsersDataStatus.collectAsState()
    val usersAuthStatus by viewModel.usersAuthStatus.collectAsState()
    val biometricState by viewModel.biometricState
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .background(Color(0xfff9f9f9)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedVisibility(visible = !viewModel.isExistingUser.value) {
            CustomTextField(
                value = viewModel.registrationUIState.value.firstName,
                onValueChange = { viewModel.onEvent(LoginSignupUIEvent.FirstNameChanged(it)) },
                label = "Name",
                keyboardType = KeyboardType.Text
            )
        }
        AnimatedVisibility(visible = !viewModel.isExistingUser.value) {
            CustomTextField(
                value = viewModel.registrationUIState.value.lastName,
                onValueChange = { viewModel.onEvent(LoginSignupUIEvent.LastNameChanged(it))},
                label = "Last Name",
                keyboardType = KeyboardType.Text
            )
        }
        CustomTextField(
            value = viewModel.registrationUIState.value.email,
            onValueChange = { viewModel.onEvent(LoginSignupUIEvent.EmailChanged(it))},
            label = "Email",
            keyboardType = KeyboardType.Email
        )
        CustomTextField(
            value = viewModel.registrationUIState.value.password,
            onValueChange = { viewModel.onEvent(LoginSignupUIEvent.PasswordChanged(it))},
            label = "Password",
            keyboardType = KeyboardType.Password,
            visualTransformation = PasswordVisualTransformation()
        )

        Button(
            onClick = {
                if (viewModel.isExistingUser.value) {
                    viewModel.onEvent(LoginSignupUIEvent.Validate)
                    if (viewModel.allValidationsPassed.value) {
                        viewModel.onEvent(LoginSignupUIEvent.LoginButtonClicked)
                    } else {
                        viewModel.onEvent(LoginSignupUIEvent.ShowDialog("Please check the data and try again"))
                    }
                } else {
                    viewModel.onEvent(LoginSignupUIEvent.Validate)
                    if (viewModel.allValidationsPassed.value) {
                        viewModel.onEvent(LoginSignupUIEvent.RegisterButtonClicked)
                    } else {
                        viewModel.onEvent(LoginSignupUIEvent.ShowDialog("Please check the data and try again"))
                    }
                }

            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, top = 4.dp, end = 8.dp),
            shape = RoundedCornerShape(6.dp),
        ) {
            Text(
                if (viewModel.isExistingUser.value) {
                    "Login"
                } else {
                    "Sign Up"
                },
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.titleMedium
            )
        }
        Text(
            text = if (viewModel.isExistingUser.value) "New to the MovieApp?" else "Already a MovieApp User?",
            modifier = Modifier
                .clickable(onClick = {
                    if (viewModel.isExistingUser.value) {
                        viewModel.onEvent(LoginSignupUIEvent.NewUser)
                    } else {
                        viewModel.onEvent(LoginSignupUIEvent.ExistingUser)
                    }
                })
                .padding(16.dp),
            style = MaterialTheme.typography.titleMedium
        )

        AnimatedVisibility(visible = viewModel.isExistingUser.value) {
            Image(
                painter = painterResource(R.drawable.fingerprint),
                contentDescription = "Biometric Logo",
                modifier = Modifier.size(128.dp)
                    .wrapContentWidth(Alignment.CenterHorizontally)
                    .clickable {
                        viewModel.authenticateUser()
                    }// Adjust the size as needed
            )
        }
        if (viewModel.showDialog.value) {
            SimpleDialog(
                message = viewModel.errorMessage.value,
                onDismiss = { viewModel.onEvent(LoginSignupUIEvent.DismissDialog)}
            )
        }

    }

    LaunchedEffect(insertUsersDataStatus) {
        when (insertUsersDataStatus) {
            is Resource.Success -> {
                navController.navigate("home")
            }

            is Resource.Error<Long> -> {
                viewModel.onEvent(LoginSignupUIEvent.ShowDialog(insertUsersDataStatus.message!!))
            }

            is Resource.Loading -> {}
        }
    }

    LaunchedEffect(usersAuthStatus) {
        when (usersAuthStatus) {
            is Resource.Success -> {
                navController.navigate("home") {
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }

            }

            is Resource.Error<User> -> {
                viewModel.onEvent(LoginSignupUIEvent.ShowDialog(usersAuthStatus.message!!))
            }

            is Resource.Loading -> {}
        }
    }

    LaunchedEffect(biometricState) {
        when (biometricState) {
            SignUpViewModel.BiometricState.Authenticating -> showBiometricPrompt(context, viewModel)
            SignUpViewModel.BiometricState.Success -> {
                navController.navigate("home") {
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            }
            else -> {
                // Show Toast message
            }
        }
    }

}

fun showBiometricPrompt(context: Context, viewModel: SignUpViewModel) {
    val executor = ContextCompat.getMainExecutor(context)
    val biometricPrompt = BiometricPrompt(
        context as FragmentActivity,
        executor,
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                viewModel.updateBiometricState(SignUpViewModel.BiometricState.Error)
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                viewModel.updateBiometricState(SignUpViewModel.BiometricState.Success)
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                viewModel.updateBiometricState(SignUpViewModel.BiometricState.Error)
            }
        })

    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle("Biometric Authentication")
        .setDescription("Log in using your biometric credential")
        .setNegativeButtonText("Use account password")
        .build()

    biometricPrompt.authenticate(promptInfo)
}