package com.example.climbing_app.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.climbing_app.AppScreens
import com.example.climbing_app.R
import com.example.climbing_app.data.Attempt
import com.example.climbing_app.data.Climb
import com.example.climbing_app.ui.ClimbViewModel
import com.example.climbing_app.ui.components.CompletionStatusIcon
import com.example.climbing_app.ui.components.RatingStars
import com.example.climbing_app.ui.components.TagListRow
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllClimbsScreen(
    climbViewModel: ClimbViewModel,
    navController: NavController,
    auth: FirebaseAuth
) {
    auth.currentUser ?: return // Return if no user
    val displayName = auth.currentUser!!.displayName

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                title = {
                    Text("$displayName - All Climbs")
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            Firebase.auth.signOut()
                            navController.navigate(
                                route = AppScreens.Login.name
                            ) {
                                popUpTo(AppScreens.Climbs.name) {
                                    inclusive = true
                                }
                            }
                        }
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp,
                            "Logout",
                            modifier = Modifier.rotate(180.0f)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("NEW") },
                icon = { Icon(Icons.Filled.Add, null) },
                onClick = { navController.navigate(route = AppScreens.Upload.name) }
            )
        }
    ) { innerPadding ->
        AllClimbsContent(
            climbViewModel = climbViewModel,
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun AllClimbsContent(
    climbViewModel: ClimbViewModel,
    navController: NavController,
    modifier: Modifier
) {
    // Get data from the ViewModel
    val attemptList by climbViewModel.allAttempts.observeAsState(initial = emptyList())

    // Image painter resource for climbs with no uploaded photo
    val placeholderPainter = painterResource(R.drawable.img_placeholder)

    // Focus manager to remove focus from the search bar on search and on climb selection
    val focusManager = LocalFocusManager.current

    // User input into search bar
    var searchQuery by rememberSaveable { mutableStateOf("") }
    val searchResults by climbViewModel.getFilteredClimbs(searchQuery).observeAsState()

    var isRefreshing by rememberSaveable { mutableStateOf(false) }

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = { isRefreshing = false },
        modifier = modifier
    ) {
        LazyColumn(
            Modifier.fillMaxSize()
        ) {
            stickyHeader {
                SearchBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    windowInsets = WindowInsets(top = 0.dp),
                    inputField = {
                        SearchBarDefaults.InputField(
                            query = searchQuery,
                            onQueryChange = {
                                searchQuery = it
                            },
                            onSearch = { focusManager.clearFocus() },
                            expanded = false,
                            onExpandedChange = {},
                            placeholder = { Text("Name, grade, uploader or tag...") },
                            leadingIcon = { Icon(Icons.Default.Search, "Search") },
                            trailingIcon = {
                                if (searchQuery.isNotEmpty()) {
                                    IconButton(
                                        onClick = { searchQuery = "" }
                                    ) {
                                        Icon(Icons.Default.Clear, "Clear")
                                    }
                                }
                            }
                        )
                    },
                    expanded = false,
                    onExpandedChange = {}
                ) {}
            }
            // Show 'no climbs' message if no climbs exist / match search
            if (searchResults == null) {
                item {
                    NoClimbsMessage(Modifier)
                }
            } else {
                if (searchResults!!.isEmpty()) {
                    item {
                        NoClimbsMessage(Modifier)
                    }
                }
                else {
                    items(searchResults!!) {
                        ClimbsListItem(
                            onClick = {
                                focusManager.clearFocus()
                                navController.navigate(
                                    route = AppScreens.Detail.name+"/${it.climbId}"
                                )
                            },
                            climb = it,
                            attempts = attemptList.filter({attempt ->
                                attempt.climbId == it.climbId && attempt.userId == Firebase.auth.currentUser?.uid
                            }),
                            placeholder = placeholderPainter,
                            climbViewModel = climbViewModel
                        )
                        HorizontalDivider(thickness = 2.dp)
                    }
                }
            }
        }
    }
}

@Composable
fun ClimbsListItem(
    onClick: () -> Unit,
    climb: Climb,
    attempts: List<Attempt>,
    placeholder: Painter,
    climbViewModel: ClimbViewModel
) {
    // Download the image for this climb
    val imageUri by climbViewModel.getClimbImage(climb).observeAsState()

    Card(
        onClick = onClick,
        shape = RectangleShape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    ) {
        Row(
            Modifier.padding(10.dp)
        ) {
            AsyncImage(
                model = imageUri,
                placeholder = placeholder,
                error = placeholder,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .aspectRatio(1f)
            )
            Column(
                Modifier
                    .padding(start = 10.dp)
                    .fillMaxWidth()
            ) {
                Row {
                    Text(
                        text = climb.name,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "by ${climb.uploader}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.padding(start = 10.dp)
                    )
                    Spacer(Modifier.weight(1.0f))
                    CompletionStatusIcon((attempts.find{attempt -> attempt.completed}) != null)
                }
                Row {
                    Text(
                        text = climb.grade,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    RatingStars(
                        rating = climb.rating,
                        modifier = Modifier.padding(start = 6.dp, top = 2.dp)
                    )
                    Spacer(modifier = Modifier.weight(1.0f))
                    Text(
                        text = "${attempts.size} attempts",
                        fontSize = 12.sp,
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                TagListRow(
                    style = climb.style,
                    holds = climb.holds,
                    incline = climb.incline,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                )
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
            text = "Try a different search or upload your first climb!",
            textAlign = TextAlign.Center,
            fontSize = 14.sp,
            fontStyle = FontStyle.Italic,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(top = 5.dp)
        )
    }
}