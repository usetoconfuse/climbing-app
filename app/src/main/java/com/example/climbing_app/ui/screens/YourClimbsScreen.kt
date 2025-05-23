package com.example.climbing_app.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import androidx.core.net.toUri
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.climbing_app.AppScreens
import com.example.climbing_app.R
import com.example.climbing_app.data.Attempt
import com.example.climbing_app.data.Climb
import com.example.climbing_app.ui.ClimbViewModel
import com.example.climbing_app.ui.components.ClimbingMajorTopAppBar
import com.example.climbing_app.ui.components.CompletionStatusIcon
import com.example.climbing_app.ui.components.RatingStars
import com.example.climbing_app.ui.components.TagListRow


@Composable
fun YourClimbsScreen(climbViewModel: ClimbViewModel, navController: NavController, userId: Int?) {
    if (userId == null) return

    Scaffold(
        topBar = {
            ClimbingMajorTopAppBar("Your Climbs")
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("NEW") },
                icon = { Icon(Icons.Filled.Add, null) },
                onClick = { navController.navigate(route = AppScreens.Upload.name+"/$userId") }
            )
        }
    ) { innerPadding ->
        YourClimbsList(
            climbViewModel = climbViewModel,
            navController = navController,
            userId = userId,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YourClimbsList(
    climbViewModel: ClimbViewModel,
    navController: NavController,
    userId: Int,
    modifier: Modifier
) {
    // Get data from the ViewModel
    val userList by climbViewModel.allUsers.observeAsState(initial = emptyList())
    val climbList by climbViewModel.allClimbs.observeAsState(initial = emptyList())
    val attemptList by climbViewModel.allAttempts.observeAsState(initial = emptyList())

    // Image painter resource for climbs with no uploaded photo
    val placeholderPainter = painterResource(R.drawable.img_placeholder)

    // Focus manager to remove focus from the search bar on search and on climb selection
    val focusManager = LocalFocusManager.current

    // User input into search bar
    var query by rememberSaveable { mutableStateOf("") }

    // Filter only items whose name, grade or tags contain the query string
    val searchResults by remember {
        derivedStateOf {
            if (query.isEmpty()) {
                climbList
            } else {
                climbList.filter {
                    it.name.contains(query, ignoreCase = true)
                    || it.grade.contains(query, ignoreCase = true)
                    || it.style.name.contains(query, ignoreCase = true)
                    || it.holds.name.contains(query, ignoreCase = true)
                    || it.incline.name.contains(query, ignoreCase = true)
                }
            }
        }
    }

    Column(
        modifier = modifier
    ) {
        SearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            windowInsets = WindowInsets(top = 0.dp),
            inputField = {
                SearchBarDefaults.InputField(
                    query = query,
                    onQueryChange = { query = it },
                    onSearch = { focusManager.clearFocus() },
                    expanded = false,
                    onExpandedChange = {},
                    placeholder = { Text("Name, grade or tag...") },
                    leadingIcon = { Icon(Icons.Default.Search, "Search") },
                    trailingIcon = {
                        if (query.isNotEmpty()) {
                            IconButton(
                                onClick = { query = "" }
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
        // Show 'no climbs' message if no climbs exist / match search
        if (searchResults.isEmpty()) {
            NoClimbsMessage(Modifier)
        } else {
            LazyColumn {
                items(searchResults) {
                    YourClimbsListItem(
                        onClick = {
                            focusManager.clearFocus()
                            navController.navigate(
                                route = AppScreens.Detail.name+"/$userId/${it.climbId}"
                            )
                        },
                        climb = it,
                        attempts = attemptList.filter({attempt ->
                            attempt.climbId == it.climbId
                                    && attempt.userId == userId
                        }),
                        uploader = userList.find{user -> user.userId == it.userId}?.username,
                        placeholder = placeholderPainter
                    )
                    HorizontalDivider(thickness = 2.dp)
                }
            }
        }
    }
}

@Composable
fun YourClimbsListItem(
    onClick: () -> Unit,
    climb: Climb,
    attempts: List<Attempt>,
    uploader: String?, // TODO use this
    placeholder: Painter
) {
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
                model = climb.imageUri.toUri(),
                placeholder = placeholder,
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
                        text = "by $uploader",
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