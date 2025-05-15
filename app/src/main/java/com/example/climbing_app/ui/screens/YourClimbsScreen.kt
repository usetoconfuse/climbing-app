package com.example.climbing_app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
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
import androidx.compose.ui.graphics.Color
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
import com.example.climbing_app.data.ClimbTag
import com.example.climbing_app.ui.ClimbViewModel
import com.example.climbing_app.ui.components.CompletedStatusLabel
import com.example.climbing_app.ui.components.RatingStars
import com.example.climbing_app.ui.components.TagListRow
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
            SnackbarHost(snackbarHostState)
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("UPLOAD") },
                icon = { Icon(Icons.Filled.Add, null) },
                onClick = {

                    // Upload a new climb
                    // TODO go to upload screen/popup
                    val pos = climbList.size + 1
                    val newClimb = ClimbData(
                        name = "Climb $pos",
                        imageResourceId = R.drawable.climb_img_1,
                        grade = "V3",
                        rating = pos % 4,
                        description = "Lorem ipsum et cetera",
                        tags = listOf(
                            ClimbTag.Powerful,
                            ClimbTag.Jugs,
                            ClimbTag.Overhang
                        )
                    )

                    climbViewModel.addClimb(newClimb)

                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = "'${newClimb.name}' uploaded successfully",
                            withDismissAction = true
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        YourClimbsList(
            climbList = climbList,
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun YourClimbsList(climbList: List<ClimbData>, navController: NavController, modifier: Modifier) {
    LazyColumn(
        modifier = modifier
    ) {
        itemsIndexed(climbList) { index, item ->
            YourClimbsListItem(navController = navController, index = index, data = item)
            HorizontalDivider(thickness = 2.dp)
        }
    }
}

@Composable
fun YourClimbsListItem(navController: NavController, index: Int, data: ClimbData) {
    Card(
        onClick = { navController.navigate(route = AppScreens.Detail.name+"/$index") },
        shape = RectangleShape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    ) {
        Row(
            Modifier.padding(10.dp)
        ) {
            Image(
                painter = painterResource(data.imageResourceId),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(120.dp)
            )
            Column(
                Modifier
                    .padding(start = 10.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = data.name,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = data.grade,
                        modifier = Modifier.padding(start = 10.dp)
                    )
                    RatingStars(
                        rating = data.rating,
                        modifier = Modifier.padding(start = 10.dp, top = 3.dp)
                    )
                }
                // TODO remove/keep this
                /* Scrapped description preview
                var previewDesc = data.description
                if (previewDesc.length > 10) {
                    previewDesc = "${previewDesc.substring(0, 15)}..."
                }
                Text(
                    text = previewDesc,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Light,
                    modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)
                ) */
                Row(
                    Modifier.padding(top = 5.dp)
                ) {
                    TagListRow(tags = data.tags)
                }
                Spacer(
                    modifier = Modifier.weight(1.0f)
                )
                Row(
                    Modifier.align(Alignment.End)
                ) {
                    CompletedStatusLabel() // TODO Fix alignment
                }
            }
        }
    }
}