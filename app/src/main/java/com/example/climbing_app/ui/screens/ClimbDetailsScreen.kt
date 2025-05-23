package com.example.climbing_app.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.climbing_app.R
import com.example.climbing_app.data.Attempt
import com.example.climbing_app.data.Climb
import com.example.climbing_app.ui.ClimbViewModel
import com.example.climbing_app.ui.components.ClimbingMinorTopAppBar
import com.example.climbing_app.ui.components.CompletionStatusLabel
import com.example.climbing_app.ui.components.RatingStars
import com.example.climbing_app.ui.components.TagListRow


@Composable
fun ClimbDetailsScreen(
    climbViewModel: ClimbViewModel,
    navController: NavController,
    userId: Int?,
    climbId: Int?
) {
    if (userId == null) return

    // Get data from the ViewModel
    val userList by climbViewModel.allUsers.observeAsState(initial = emptyList())
    val climbList by climbViewModel.allClimbs.observeAsState(initial = emptyList())
    val attemptList by climbViewModel.allAttempts.observeAsState(initial = emptyList())

    // Find the data for climb we're viewing
    val user = userList.find{ user -> user.userId == userId }
    if (user == null) return
    val climb = climbList.find{ climb -> climb.climbId == climbId }
    val attempts = attemptList.filter{ attempt -> attempt.climbId == climbId}

    Scaffold(
        topBar = {
            ClimbingMinorTopAppBar(climb?.name ?: "Not Found", navController)
        },
    ) { innerPadding ->
        // Don't load the page if data wasn't retrieved
        if (climb == null) {
            ClimbNotFoundMessage(Modifier.padding(innerPadding))
        } else {
            ClimbDetailsContent(
                modifier = Modifier.padding(innerPadding),
                climb = climb,
                uploader = user.username,
                attempts = attempts
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ClimbDetailsContent(
    modifier: Modifier,
    climb: Climb,
    uploader: String,
    attempts: List<Attempt>
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        // Climb info
        Column(
            Modifier.padding(start = 16.dp, end = 16.dp)
        ) {
            AsyncImage(
                model = climb.imageUri.toUri(),
                placeholder = painterResource(R.drawable.img_placeholder),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(150.dp)
                    .aspectRatio(1f)
                    .align(Alignment.CenterHorizontally)
            )
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Column(
                    Modifier.weight(3.0f)
                ) {
                    FlowRow {
                        Text(
                            text = climb.name,
                            fontSize = 20.sp,
                            modifier = Modifier.padding(end = 10.dp)
                        )
                        Text(
                            text = climb.grade,
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.padding(end = 10.dp)
                        )
                        RatingStars(
                            rating = climb.rating,
                            modifier = Modifier.padding(top = 2.dp, end = 10.dp)
                        )
                        Text(
                            text = "by $uploader",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                    Text(
                        text = "uploaded on ${climb.formattedUploadDate()} at ${climb.formattedUploadTime()}",
                        fontSize = 12.sp,
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.padding(start = 2.dp)
                    )
                }
                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier.weight(2.0f)
                ) {
                    CompletionStatusLabel(climb.isComplete)
                    Text(
                        text = "${attempts.size} attempts",
                        fontSize = 12.sp,
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.padding(top = 2.dp, end = 2.dp)
                    )
                }
            }
            Text(
                text = climb.description,
                fontSize = 14.sp,
                modifier = Modifier.padding(start = 2.dp)
            )
            TagListRow(
                style = climb.style,
                holds = climb.holds,
                incline = climb.incline,
                modifier = Modifier
                    .width(250.dp)
                    .padding(top = 10.dp)
            )
        }
        HorizontalDivider(
            thickness = 2.dp,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            text = "History",
            color = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier.padding(top = 8.dp, start = 18.dp)
        )
        /* Activity list component */
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
