package com.example.climbing_app

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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


class MainActivity : ComponentActivity() {
    // TODO change ViewModel to use lifecycle extension functions
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
                    // ViewModel
                    val climbViewModel = ClimbViewModel(application)

                    // Navigation framework
                    val navController: NavHostController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = AppScreens.Login.name
                    ) {
                        // Login
                        composable(
                            route = AppScreens.Login.name
                        ) {
                            LoginScreen(climbViewModel, navController)
                        }
                        // Your climbs
                        composable(
                            route = AppScreens.Climbs.name+"/{user}",
                            arguments = listOf(
                                navArgument(name = "user") {
                                    type = NavType.IntType
                                }
                            )
                        ) {index ->
                            AllClimbsScreen(
                                climbViewModel,
                                navController,
                                userId = index.arguments?.getInt("user"))
                        }
                        // Upload climb, this is a dialog so it appears over the previous screen
                        composable(
                            route = AppScreens.Upload.name+"/{user}",
                            arguments = listOf(
                                navArgument(name = "user") {
                                    type = NavType.IntType
                                }
                            )
                        ) {
                            index ->
                            UploadClimbScreen(
                                climbViewModel,
                                navController,
                                userId = index.arguments?.getInt("user")
                            )
                        }
                        // Climb details
                        composable(
                            route = AppScreens.Detail.name+"/{user}/{climb}",
                            arguments = listOf(
                                navArgument(name = "user") {
                                  type = NavType.IntType
                                },
                                navArgument(name = "climb") {
                                    type = NavType.IntType
                                }
                            )
                        ) {
                            index ->
                            ClimbDetailsScreen(
                                climbViewModel,
                                navController,
                                userId = index.arguments?.getInt("user"),
                                climbId = index.arguments?.getInt("climb")
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
