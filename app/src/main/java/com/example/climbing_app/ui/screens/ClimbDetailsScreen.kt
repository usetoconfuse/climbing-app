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
import com.example.climbing_app.data.Climb
import com.example.climbing_app.ui.ClimbViewModel
import com.example.climbing_app.ui.components.ClimbingMinorTopAppBar
import com.example.climbing_app.ui.components.CompletionStatusLabel
import com.example.climbing_app.ui.components.RatingStars
import com.example.climbing_app.ui.components.TagListRow


@Composable
fun ClimbDetailsScreen(climbViewModel: ClimbViewModel, navController: NavController, id: Int?) {

    // Get all climbs from the ViewModel
    val climbList by climbViewModel.allClimbs.observeAsState(initial = emptyList())

    // Find the climb we're viewing
    val climb = climbList.find{ climb -> climb.id == id }

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
                data = climb
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ClimbDetailsContent(modifier: Modifier, data: Climb) {
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
                model = data.imageUri.toUri(),
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
                            text = data.name,
                            fontSize = 20.sp,
                            modifier = Modifier.padding(end = 10.dp)
                        )
                        Text(
                            text = data.grade,
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.padding(end = 10.dp)
                        )
                        RatingStars(
                            rating = data.rating,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                    Text(
                        text = "uploaded on ${data.formattedUploadDate()} at ${data.formattedUploadTime()}",
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
                    CompletionStatusLabel(data.isComplete)
                    Text(
                        text = "${data.attempts} attempts",
                        fontSize = 12.sp,
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.padding(top = 2.dp, end = 2.dp)
                    )
                }
            }
            Text(
                text = data.description,
                fontSize = 14.sp,
                modifier = Modifier.padding(start = 2.dp)
            )
            TagListRow(
                style = data.style,
                holds = data.holds,
                incline = data.incline,
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
