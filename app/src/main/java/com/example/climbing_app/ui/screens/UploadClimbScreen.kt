package com.example.climbing_app.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.climbing_app.R
import com.example.climbing_app.data.Climb
import com.example.climbing_app.data.ClimbTagHolds
import com.example.climbing_app.data.ClimbTagIncline
import com.example.climbing_app.data.ClimbTagStyle
import com.example.climbing_app.ui.ClimbViewModel
import com.example.climbing_app.ui.components.ClimbingTopAppBar


@Composable
fun UploadClimbScreen(climbViewModel: ClimbViewModel, navController: NavController) {

    // Get all climbs from the ViewModel
    val climbList by climbViewModel.allClimbs.observeAsState(initial = emptyList())

    // Get context for toast
    val context = LocalContext.current

    Scaffold(
        topBar = {
            ClimbingTopAppBar("New Climb")
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("UPLOAD") },
                icon = { Icon(Icons.Filled.Add, null) },
                onClick = {

                    // Upload a new climb
                    // TODO real upload form
                    val pos = climbList.size + 1
                    val newClimb = Climb(
                        name = "La Dérive des Incontinents $pos",
                        imageResourceId = R.drawable.climb_img_1,
                        grade = "V3",
                        rating = pos % 4,
                        description = "Lorem ipsum et cetera",
                        style = ClimbTagStyle.Technical,
                        holds = ClimbTagHolds.Slopers,
                        incline = ClimbTagIncline.Overhang
                    )

                    climbViewModel.insert(newClimb)
                    Toast.makeText(
                        context,
                        "${newClimb.name} uploaded successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    navController.popBackStack()
                }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            Modifier.padding(innerPadding)
        ) {
            Text("Upload")
        }
    }
}