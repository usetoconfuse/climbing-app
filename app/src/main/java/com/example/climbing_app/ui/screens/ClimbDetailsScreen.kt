package com.example.climbing_app.ui.screens

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.climbing_app.R
import com.example.climbing_app.data.Attempt
import com.example.climbing_app.data.Climb
import com.example.climbing_app.ui.ClimbViewModel
import com.example.climbing_app.ui.components.ClimbingMinorTopAppBar
import com.example.climbing_app.ui.components.CompletionStatusIcon
import com.example.climbing_app.ui.components.CompletionStatusLabel
import com.example.climbing_app.ui.components.RatingStars
import com.example.climbing_app.ui.components.TagListRow
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch
import java.util.Locale


@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClimbDetailsScreen(
    climbViewModel: ClimbViewModel,
    navController: NavController,
    auth: FirebaseAuth,
    climbId: String?
) {
    auth.currentUser ?: return // Return if no user
    val displayName = auth.currentUser!!.displayName

    if (climbId == null) return // Return if no climb
    val context = LocalContext.current

    var openDialogType by rememberSaveable { mutableStateOf("") }

    // Get data from the ViewModel
    val climbList by climbViewModel.allClimbs.observeAsState(initial = emptyList())
    val attemptList by climbViewModel.allAttempts.observeAsState(initial = emptyList())

    // Find the data for climb we're viewing
    val climb = climbList.find { climb ->
        climb.climbId == climbId
    }
    val attempts = attemptList.filter {attempt ->
        attempt.climbId == climbId && attempt.userId == Firebase.auth.currentUser?.uid
    }

    // Download the image for this climb
    val imageUri by climbViewModel.getClimbImage(climb ?: Climb()).observeAsState()

    // Helper function for toast message
    val capitalizeFirst: (String) -> String = { str ->
        str.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale.getDefault()
            ) else it.toString()
        }
    }

    // Variables for refresh
    val coroutineScope = rememberCoroutineScope()
    var isRefreshing by rememberSaveable { mutableStateOf(false) }
    val state = rememberPullToRefreshState()

    // Dialogs for logging attempt confirmation
    when {
        openDialogType.isNotEmpty() && climb != null -> {
            BasicAlertDialog(
                onDismissRequest = {
                    openDialogType = ""
                }
            ) {
                Surface(
                    modifier = Modifier
                        .wrapContentWidth()
                        .wrapContentHeight(),
                    shape = MaterialTheme.shapes.large,
                    tonalElevation = AlertDialogDefaults.TonalElevation
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Log $openDialogType for ${climb.name}?",
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Row(
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            TextButton(
                                onClick = { openDialogType = "" }
                            ) {
                                Text("Cancel")
                            }
                            TextButton(
                                onClick = {
                                    val newAttempt = Attempt(
                                        climbId = climb.climbId!!,
                                        completed = openDialogType == "send"
                                    )

                                    climbViewModel.insertAttempt(newAttempt)
                                    Toast.makeText(
                                        context,
                                        "${capitalizeFirst(openDialogType)} logged for ${climb.name}",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    openDialogType = ""
                                },
                            ) {
                                Text("Confirm")
                            }
                        }
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            ClimbingMinorTopAppBar("$displayName - Viewing " + (climb?.name ?: "unknown"), navController)
        },
        bottomBar = {
            // Bottom log buttons
            BottomAppBar(
                contentPadding = PaddingValues(0.dp)
            ) {
                Button(
                    onClick = { openDialogType = "attempt" },
                    shape = RectangleShape,
                    modifier = Modifier
                        .weight(1.0f)
                        .fillMaxSize()
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = null
                    )
                    Text(
                        text = "LOG ATTEMPT",
                        modifier = Modifier.padding(top = 2.dp, start = 6.dp)
                    )
                }
                Button(
                    onClick = { openDialogType = "send" },
                    shape = RectangleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF78A55A)),
                    modifier = Modifier
                        .weight(1.0f)
                        .fillMaxSize()
                ) {
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = null,
                        tint = Color.White
                    )
                    Text(
                        text = "LOG SEND",
                        color = Color.White,
                        modifier = Modifier.padding(top = 2.dp, start = 6.dp)
                    )
                }
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = {
                isRefreshing = true
                coroutineScope.launch { state.animateToHidden() }
                isRefreshing = false
            },
            state = state,
            modifier = Modifier.padding(innerPadding)
        ) {
            ClimbDetailsContent(
                context = context,
                climb = climb,
                attempts = attempts,
                imageUri = imageUri
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ClimbDetailsContent(
    context: Context,
    climb: Climb?,
    attempts: List<Attempt>,
    imageUri: Uri?
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        // Don't load the page if data wasn't retrieved
        if (climb == null) {
            item {
                ClimbNotFoundMessage()
            }
        } else {
            // Main climb info
            item {
                ClimbDetailsMainInfo(
                    context = context,
                    climb = climb,
                    attempts = attempts,
                    placeholderPainter = painterResource(R.drawable.img_placeholder),
                    imageUri = imageUri
                )
            }
            // List of attempts
            itemsIndexed(attempts) { index, item ->
                AttemptHistoryItem(attempts.size-index, item)
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ClimbDetailsMainInfo(
    context: Context,
    climb: Climb,
    attempts: List<Attempt>,
    placeholderPainter: Painter,
    imageUri: Uri?
) {
    Column(
        modifier = Modifier
            .padding(top = 16.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Climb info
        Column(
            Modifier.padding(start = 16.dp, end = 16.dp)
        ) {
            AsyncImage(
                model = imageUri,
                placeholder = placeholderPainter,
                error = placeholderPainter,
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
                            text = "by ${climb.uploader}",
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
                    CompletionStatusLabel((attempts.find{attempt -> attempt.completed}) != null)
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
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                TagListRow(
                    style = climb.style,
                    holds = climb.holds,
                    incline = climb.incline,
                    modifier = Modifier
                        .width(220.dp)
                        .padding(top = 10.dp)
                )
                Spacer(Modifier.weight(1.0f))
                // Share button with ShareSheet
                TextButton(
                    onClick = {
                        val sendIntent: Intent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TITLE, "Share climb via")
                            putExtra(
                                Intent.EXTRA_TEXT,
                                "Check out this climb from Sendtrain!\n" +
                                        "${climb.name} by ${climb.uploader}" +
                                        " | ${climb.grade} | ${climb.rating} stars\n" +
                                        "I've attempted this climb ${attempts.size} times!"
                            )
                            type = "text/plain"
                        }

                        val shareIntent = Intent.createChooser(sendIntent, "Share climb via")
                        context.startActivity(shareIntent)
                    }
                ) {
                    Icon(Icons.Default.Share, null)
                    Text(
                        text = "SHARE",
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
        HorizontalDivider(
            thickness = 2.dp,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            text = "History",
            color = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier.padding(top = 8.dp, start = 18.dp, bottom = 8.dp)
        )
    }
}

@Composable
fun AttemptHistoryItem(attemptNum: Int, attempt: Attempt) {
    Row(
        Modifier.padding(vertical = 4.dp, horizontal = 16.dp)
    ) {
        CompletionStatusIcon(attempt.completed)
        Text(
            text = "Attempt $attemptNum",
            modifier = Modifier.padding(start = 10.dp)
        )
        Spacer(Modifier.weight(1.0f))
        Text(
            text = "${attempt.formattedUploadTime()}    ${attempt.formattedUploadDate()}",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.secondary
        )
    }
    HorizontalDivider(thickness = 2.dp)
}

@Composable
fun ClimbNotFoundMessage() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
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
