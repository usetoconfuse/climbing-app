package com.example.climbing_app

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.climbing_app.ui.ClimbViewModel
import com.example.climbing_app.ui.screens.AllClimbsScreen
import com.example.climbing_app.ui.screens.ClimbDetailsScreen
import com.example.climbing_app.ui.screens.LoginScreen
import com.example.climbing_app.ui.screens.UploadClimbScreen
import com.example.climbing_app.ui.theme.ClimbingappTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.auth


class MainActivity : ComponentActivity() {
    @SuppressLint("ViewModelConstructorInComposable")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ClimbingappTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Firebase authentication
                    val auth = Firebase.auth
                    val user = auth.currentUser

                    // ViewModel
                    val climbViewModel = ClimbViewModel(application)

                    // Navigation framework
                    val navController: NavHostController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = if (user != null) {
                                AppScreens.Climbs.name
                            } else {
                                AppScreens.Login.name
                            },
                        enterTransition = { EnterTransition.None },
                        exitTransition = { ExitTransition.None }
                    ) {
                        // Login
                        composable(
                            route = AppScreens.Login.name
                        ) {
                            LoginScreen(navController)
                        }
                        // Your climbs
                        composable(
                            route = AppScreens.Climbs.name,
                        ) {
                            AllClimbsScreen(
                                climbViewModel,
                                navController
                            )
                        }
                        // Upload climb, this is a dialog so it appears over the previous screen
                        composable(
                            route = AppScreens.Upload.name
                        ) {
                            UploadClimbScreen(
                                climbViewModel,
                                navController
                            )
                        }
                        // Climb details
                        composable(
                            route = AppScreens.Detail.name+"/{climb}",
                            arguments = listOf(
                                navArgument(name = "climb") {
                                    type = NavType.StringType
                                }
                            )
                        ) {
                            index ->
                            ClimbDetailsScreen(
                                climbViewModel,
                                navController,
                                climbId = index.arguments?.getString("climb")
                            )
                        }
                    }
                }
            }
        }
    }
}

enum class AppScreens {
    Login,
    Climbs,
    Upload,
    Detail
}
