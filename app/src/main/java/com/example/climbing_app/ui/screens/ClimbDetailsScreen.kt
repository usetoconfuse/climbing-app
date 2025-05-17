package com.example.climbing_app.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        if (climb == null) {
            ClimbNotFoundMessage(Modifier.padding(innerPadding))
        } else {
            ClimbDetailsContent(Modifier.padding(innerPadding))
        }
    }
}

@Composable
fun ClimbDetailsContent(modifier: Modifier) {
    Column(
        modifier = modifier
    ) {
        // Screen content
    }
}

@Composable
fun ClimbNotFoundMessage(modifier: Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(top = 24.dp)
            .fillMaxWidth()
    ) {
        Icon(
            modifier = Modifier.size(72.dp),
            imageVector = Icons.Filled.Warning,
            contentDescription = null
        )
        Text(
            text = "Climb not found",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 10.dp),
        )
        Text(
            text = "Couldn't find the climb you're looking for.\r\nTry selecting another climb.",
            textAlign = TextAlign.Center,
            fontSize = 14.sp,
            fontStyle = FontStyle.Italic,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(top = 5.dp)
        )
    }
}
