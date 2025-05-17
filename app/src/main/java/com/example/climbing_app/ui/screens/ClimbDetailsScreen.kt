package com.example.climbing_app.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import com.example.climbing_app.ui.ClimbViewModel
import com.example.climbing_app.ui.components.ClimbingTopAppBar


@Composable
fun ClimbDetailsScreen(climbViewModel: ClimbViewModel, id: Int?) {

    // Get all climbs from the ViewModel
    val climbList by climbViewModel.allClimbs.observeAsState(initial = emptyList())

    // Find the climb we're viewing
    val climb = climbList.find{ climb -> climb.id == id }

    Scaffold(
        topBar = {
            ClimbingTopAppBar("Details")
        },
    ) { innerPadding ->
        // Don't load the page if data wasn't retrieved
        if (climb == null) Text("Climb not found") else

        Text(modifier = Modifier.padding(innerPadding), text = "ID: $id, name: ${climb.name}")

    }
}