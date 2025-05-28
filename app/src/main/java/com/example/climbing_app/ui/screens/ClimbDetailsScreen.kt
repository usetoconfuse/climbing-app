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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.climbing_app.ui.components.AnchoredDraggableBox
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
    val coroutineScope = rememberCoroutineScope()

    // Dialog state variables
    // Type of dialog currently showing
    var openDialogType by remember { mutableStateOf("") }
    // Attempt to delete if deleting an attempt
    var attemptIdToDelete by remember { mutableIntStateOf(-1) }

    // Get data from the ViewModel
    val climbList by climbViewModel.allClimbs.observeAsState(initial = emptyList())
    val attemptList by climbViewModel.allAttempts.observeAsState(initial = emptyList())

    // Find the data for climb we're viewing
    val climb by remember {
        derivedStateOf {
            climbList.find { climb ->
                climb.climbId == climbId
            }
        }
    }

    val attempts by remember {
        derivedStateOf {
            attemptList.filter { attempt ->
                attempt.climbId == climbId && attempt.userId == Firebase.auth.currentUser?.uid
            }
        }
    }

    // Download the image for this climb
    val imageUri by climbViewModel.getClimbImage(climb).observeAsState()

    // Variables for refresh
    var isRefreshing by rememberSaveable { mutableStateOf(false) }
    val state = rememberPullToRefreshState()

    // Delete attempt function, callback for swipe-to-delete rows
    fun showDeleteAttemptDialog(attempt: Attempt) {
        attemptIdToDelete = attempt.attemptId
        openDialogType = "delete"
    }

    // Callback functions for the confirmation dialog
    fun logAttempt() {
        val newAttempt = Attempt(
            climbId = climb!!.climbId!!,
            completed = openDialogType == "send"
        )

        climbViewModel.insertAttempt(newAttempt)
    }

    fun deleteAttempt() {
        val attempt = attempts.find {attempt -> attempt.attemptId == attemptIdToDelete}

        // Ensure we have an attempt to delete
        if (attempt != null) {
            climbViewModel.deleteAttempt(attempt)
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
            // Dialogs for logging and deleting attempt confirmation
            ClimbDetailsDialog(
                openDialogType = openDialogType,
                climbName = climb?.name,
                onSuccess = if (openDialogType == "delete") ::deleteAttempt else ::logAttempt,
                onClear = {
                    openDialogType = ""
                    attemptIdToDelete = -1
                }
            )
            ClimbDetailsContent(
                context = context,
                climb = climb,
                attempts = attempts,
                onDelete = ::showDeleteAttemptDialog,
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
    onDelete: (Attempt) -> Unit,
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
                HorizontalDivider(thickness = 1.dp)
                AttemptHistoryItem(attempts.size-index, item, onDelete)
            }
            item {
                HorizontalDivider(thickness = 1.dp)
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
        Row(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 18.dp)
        ) {
            Text(
                text = "History",
                color = MaterialTheme.colorScheme.tertiary
            )
            Spacer(Modifier.weight(1.0f))
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                tint = MaterialTheme.colorScheme.tertiary,
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 5.dp, end = 4.dp)
                    .size(14.dp)
            )
            Text(
                text = "Swipe to delete",
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.tertiary
            )
        }
    }
}

@Composable
fun AttemptHistoryItem(attemptNum: Int, attempt: Attempt, onDelete: (Attempt) -> Unit) {
    AnchoredDraggableBox(
        modifier = Modifier.fillMaxWidth(),
        firstContent = { modifier ->
            Row(
                modifier = modifier
                    .padding(horizontal = 16.dp)
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
        },
        secondContent = { modifier ->
            IconButton(
                onClick = {},
                modifier = modifier
                    .width(50.dp)
                    .background(Color(0xFFF44336))
            ) {
                Icon(Icons.Default.Delete, "Delete")
            }
        },
        secondContentCover = { modifier ->
            IconButton(
                onClick = {},
                modifier = modifier
                    .width(50.dp)
                    .background(MaterialTheme.colorScheme.background)
            ) {}
        },
        offsetSize = 50.dp,
        onDragComplete = { onDelete(attempt) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClimbDetailsDialog(
    openDialogType: String,
    climbName: String?,
    onSuccess: () -> Unit,
    onClear: () -> Unit
) {
    if (openDialogType.isEmpty() || climbName == null) return

    // Helper function for toast message
    val capitalizeFirst: (String) -> String = { str ->
        str.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale.getDefault()
            ) else it.toString()
        }
    }

    val context = LocalContext.current

    val dialogText: String
    val toastText: String
    val confirmText: String
    val confirmTextColor: Color

    if (openDialogType == "delete") {
        dialogText = "Delete attempt for $climbName?"
        toastText = "Attempt deleted for $climbName"
        confirmText = "DELETE"
        confirmTextColor = Color(0xFFF44336)
    } else {
        dialogText = "Log $openDialogType for $climbName?"
        toastText = "${capitalizeFirst(openDialogType)} logged for $climbName"
        confirmText = if (openDialogType == "send") "SEND IT" else "LOG ATTEMPT"
        confirmTextColor = if (openDialogType == "send") Color(0xFF78A55A) else Color.Unspecified
    }

    BasicAlertDialog(
        onDismissRequest = { onClear() }
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
                    text = dialogText,
                )
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.align(Alignment.End)
                ) {
                    TextButton(
                        onClick = { onClear() }
                    ) {
                        Text("CANCEL")
                    }
                    TextButton(
                        onClick = {
                            onSuccess()

                            Toast.makeText(
                                context,
                                toastText,
                                Toast.LENGTH_SHORT
                            ).show()

                            onClear()
                        },
                    ) {
                        Text(text = confirmText, color = confirmTextColor)
                    }
                }
            }
        }
    }
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
