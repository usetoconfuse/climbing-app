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
import com.example.climbing_app.ui.screens.ClimbDetailsScreen
import com.example.climbing_app.ui.ClimbViewModel
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
                    val climbViewModel = ClimbViewModel()

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
                        // Climb details
                        composable(
                            route = AppScreens.Detail.name+"/{index}",
                            arguments = listOf(
                                navArgument(name = "index") {
                                    type = NavType.IntType
                                }
                            )
                        ) {
                            index ->
                            ClimbDetailsScreen(
                                climbViewModel,
                                index = index.arguments?.getInt("index")
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
    Detail
}
