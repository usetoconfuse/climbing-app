package com.example.climbing_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.climbing_app.ui.theme.ClimbingappTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ClimbingappTheme {
                YourClimbsScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YourClimbsScreen() {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                ),
                title = {
                    Text("Your Uploaded Climbs")
                },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Filled.Menu, "Menu")
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("UPLOAD") },
                icon = { Icon(Icons.Filled.Add, null) },
                onClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = "Uploaded!!!!",
                            actionLabel = "DISMISS"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        ClimbList(
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun ClimbList(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier
    ) {
        items(20) { index ->
            ClimbItem(index = index+1)
            HorizontalDivider(thickness = 2.dp)
        }
    }
}

@Composable
fun ClimbItem(index: Int) {
    Row(
        Modifier.padding(10.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.bouldering_wall),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(100.dp)
        )
        ClimbItemRight(index = index)
    }
}

@Composable
fun ClimbItemRight(index : Int) {
    Column(
        Modifier.padding(start = 10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.Top
        ) {
            Text(text = "Climb $index", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(modifier = Modifier.padding(start = 10.dp), text="V3")
            RatingStars(modifier = Modifier.padding(start = 10.dp, top = 3.dp), rating = index % 4)
        }
    }
}

@Composable
fun RatingStars(modifier: Modifier = Modifier, rating : Int) {
    Row(
        modifier = modifier
    ) {
        for (i in 1..3) {
            if (i <= rating) {
                Icon(
                    modifier = Modifier.size(18.dp),
                    imageVector = Icons.Filled.Star,
                    contentDescription = null
                )
            }
        }
    }
}