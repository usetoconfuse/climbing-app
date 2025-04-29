package com.example.climbing_app.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.climbing_app.ui.ClimbViewModel


@Composable
fun ClimbDetailsScreen(climbViewModel: ClimbViewModel, index: Int?) {
    Scaffold { innerPadding ->
        val climbList by climbViewModel.climbList.collectAsState(emptyList())
        val size = climbList.size
        Text(modifier = Modifier.padding(innerPadding), text = "Index: $index, Climbs: $size")
    }
}