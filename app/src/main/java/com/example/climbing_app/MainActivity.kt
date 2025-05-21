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
import com.example.climbing_app.ui.screens.ClimbDetailsScreen
import com.example.climbing_app.ui.screens.UploadClimbScreen
import com.example.climbing_app.ui.screens.YourClimbsScreen
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
                        startDestination = AppScreens.Climbs.name
                    ) {
                        // Your climbs
                        composable(
                            route = AppScreens.Climbs.name
                        ) {
                            YourClimbsScreen(climbViewModel, navController)
                        }
                        // Upload climb, this is a dialog so it appears over the previous screen
                        composable(
                            route = AppScreens.Upload.name,
                        ) {
                            UploadClimbScreen(climbViewModel, navController)
                        }
                        // Climb details
                        composable(
                            route = AppScreens.Detail.name+"/{id}",
                            arguments = listOf(
                                navArgument(name = "id") {
                                    type = NavType.IntType
                                }
                            )
                        ) {
                            id ->
                            ClimbDetailsScreen(
                                climbViewModel,
                                navController,
                                id = id.arguments?.getInt("id")
                            )
                        }
                    }
                }
            }
        }
    }
}

enum class AppScreens {
    Climbs,
    Upload,
    Detail
}
