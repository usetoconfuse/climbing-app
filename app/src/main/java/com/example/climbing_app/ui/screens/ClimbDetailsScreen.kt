package com.example.climbing_app.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import com.example.climbing_app.ui.ClimbViewModel


@Composable
fun ClimbDetailsScreen(climbViewModel: ClimbViewModel, index: Int?) {
    Scaffold { innerPadding ->
        // Observe UI state from the ViewModel
        val climbList by climbViewModel.allClimbs.observeAsState(initial = emptyList())
        val size = climbList.size
        Text(modifier = Modifier.padding(innerPadding), text = "Index: $index, Climbs: $size")
    }
}