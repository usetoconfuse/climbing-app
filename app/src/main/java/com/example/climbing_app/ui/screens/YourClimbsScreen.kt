package com.example.climbing_app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.climbing_app.AppScreens
import com.example.climbing_app.R
import com.example.climbing_app.data.Climb
import com.example.climbing_app.data.ClimbTagHolds
import com.example.climbing_app.data.ClimbTagIncline
import com.example.climbing_app.data.ClimbTagStyle
import com.example.climbing_app.ui.ClimbViewModel
import com.example.climbing_app.ui.components.ClimbingTopAppBar
import com.example.climbing_app.ui.components.CompletedStatusIcon
import com.example.climbing_app.ui.components.RatingStars
import com.example.climbing_app.ui.components.TagListRow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Composable
fun YourClimbsScreen(climbViewModel: ClimbViewModel, navController: NavController) {

    // Get all climbs from the ViewModel
    val climbList by climbViewModel.allClimbs.observeAsState(initial = emptyList())

    // Snackbar objects
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            ClimbingTopAppBar("Your Climbs")
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
                    val newClimb = Climb(
                        name = "Climb $pos",
                        imageResourceId = R.drawable.climb_img_1,
                        grade = "V3",
                        rating = pos % 4,
                        description = "Lorem ipsum et cetera",
                        style = ClimbTagStyle.Powerful,
                        holds = ClimbTagHolds.Slopers,
                        incline = ClimbTagIncline.Overhang
                    )

                    climbViewModel.insert(newClimb)

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
        // Show 'no climbs' message if no climbs exist
        if (climbList.isEmpty()) {
            NoClimbsMessage(Modifier.padding(innerPadding))
        } else {
            YourClimbsList(
                climbList = climbList,
                navController = navController,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
fun YourClimbsList(climbList: List<Climb>, navController: NavController, modifier: Modifier) {
    LazyColumn(
        modifier = modifier
    ) {
        items(climbList) { climb ->
            YourClimbsListItem(navController = navController, data = climb)
            HorizontalDivider(thickness = 2.dp)
        }
    }
}

@Composable
fun YourClimbsListItem(navController: NavController, data: Climb) {

    Card(
        onClick = { navController.navigate(route = AppScreens.Detail.name+"/${data.id}") },
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
                modifier = Modifier.size(90.dp)
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
                    Spacer(Modifier.weight(1.0f))
                    CompletedStatusIcon()
                }
                Row {
                    Text(
                        text = "uploaded on ${data.formattedUploadDate()}",
                        fontSize = 12.sp,
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(Modifier.weight(1.0f))
                    Text(
                        text = "${data.attempts} attempts",
                        fontSize = 12.sp,
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                Row(
                    Modifier.padding(top = 15.dp)
                ) {
                    TagListRow(
                        style = data.style,
                        holds = data.holds,
                        incline = data.incline
                    )
                }
            }
        }
    }
}

@Composable
fun NoClimbsMessage(modifier: Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(top = 24.dp)
            .fillMaxWidth()
    ) {
        Icon(
            modifier = Modifier.size(72.dp),
            imageVector = Icons.Filled.Search,
            contentDescription = null
        )
        Text(
            text = "No climbs found",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 10.dp),
        )
        Text(
            text = "You haven't uploaded any climbs yet.\r\nPress the upload button to get started!",
            textAlign = TextAlign.Center,
            fontSize = 14.sp,
            fontStyle = FontStyle.Italic,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(top = 5.dp)
        )
    }
}