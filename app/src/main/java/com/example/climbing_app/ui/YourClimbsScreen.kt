package com.example.climbing_app.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.climbing_app.AppScreens
import com.example.climbing_app.R
import com.example.climbing_app.data.ClimbData
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YourClimbsScreen(climbViewModel: ClimbViewModel, navController: NavController) {

    // Observe UI state from the ViewModel
    val climbList by climbViewModel.climbList.collectAsState(emptyList())

    // Snackbar objects
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                title = {
                    Text("Your Climbs")
                },
                navigationIcon = {
                    IconButton(onClick = { /* open nav drawer */ }) {
                        Icon(Icons.Filled.Menu, "Menu")
                    }
                },
                actions = {
                    IconButton(onClick = { /* change theme */ }) {
                        Icon(Icons.Filled.Settings, "Change Theme")
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
                    val pos = climbList.size + 1
                    val newClimb = ClimbData(
                        name = "Climb $pos",
                        grade = "V3",
                        imageResourceId = R.drawable.climb_img_1,
                        rating = pos % 4,
                        description = "Description"
                    )

                    climbViewModel.addClimb(newClimb)

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
        ClimbListColumn(
            climbList = climbList,
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun ClimbListColumn(climbList: List<ClimbData>, navController: NavController, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier
    ) {
        itemsIndexed(climbList) { index, item ->
            ClimbListItem(navController = navController, index = index, data = item)
            HorizontalDivider(thickness = 2.dp)
        }
    }
}

@Composable
fun ClimbListItem(navController: NavController, index: Int, data: ClimbData) {
    Card(
        onClick = { navController.navigate(route = AppScreens.Detail.name+"/$index") },
        shape = RectangleShape,
        modifier = Modifier.fillMaxSize(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    ) {
        Row(
            modifier = Modifier.padding(10.dp)
        ) {
            Image(
                painter = painterResource(data.imageResourceId),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(100.dp)
            )
            Column(
                Modifier.padding(start = 10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.Top
                ) {
                    Text(text = data.name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text(text = data.grade, modifier = Modifier.padding(start = 10.dp))
                    RatingStars(modifier = Modifier.padding(start = 10.dp, top = 3.dp), rating = data.rating)
                }
            }
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