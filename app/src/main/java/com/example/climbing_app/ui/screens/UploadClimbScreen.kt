package com.example.climbing_app.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadClimbScreen(climbViewModel: ClimbViewModel, navController: NavController) {

    // Get all climbs from the ViewModel
    val climbList by climbViewModel.allClimbs.observeAsState(initial = emptyList())

    // Get context for toast
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                title = {
                    Text("New Climb")
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            Modifier.padding(innerPadding)
        ) {
            Text("Upload")
            Button(
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
            ) {
                Text("UPLOAD")
            }
        }
    }
}