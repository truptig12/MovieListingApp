package com.frogsocial.moviesapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.frogsocial.auth_presentation.SignUpViewModel
import com.frogsocial.auth_presentation.SignUpViewModelNew
import com.frogsocial.auth_presentation.screen.SignInScreen
import com.frogsocial.auth_presentation.screen.SignUpScreen
import com.frogsocial.movie_presentation.HomeScreen
import com.frogsocial.moviesapp.ui.theme.MoviesAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MoviesAppTheme {
                val viewModel: com.frogsocial.movie_presentation.MovieViewModel = hiltViewModel()
                 val viewModelSign: SignUpViewModelNew by viewModels()

                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "signup") {
                    composable("signup") {
                        SignInScreen(viewModelSign, navController)
                    }
                    composable("home") {
                        HomeScreen(viewModel,navController)
                    }
                }

            }
        }
    }
}

