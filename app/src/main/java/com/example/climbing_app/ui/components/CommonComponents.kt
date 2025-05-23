package com.example.climbing_app.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.climbing_app.data.ClimbTag
import com.example.climbing_app.data.ClimbTagHolds
import com.example.climbing_app.data.ClimbTagIncline
import com.example.climbing_app.data.ClimbTagStyle


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClimbingMajorTopAppBar(title: String) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        title = {
            Text(title)
        },
        navigationIcon = {
            IconButton(onClick = { /* go to login */ }) {
                Icon(Icons.AutoMirrored.Filled.ExitToApp,
                    "Logout",
                    modifier = Modifier.rotate(180.0f)
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClimbingMinorTopAppBar(title: String, navController: NavController) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        title = {
            Text(title)
        },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
            }
        }
    )
}

@Composable
fun TagListRow(style: ClimbTagStyle,
               holds: ClimbTagHolds,
               incline: ClimbTagIncline,
               modifier: Modifier) {
    Row(
        modifier = modifier
    ) {
        for (i in listOf(style, holds, incline)) {
            Box(
                Modifier.weight(1.0f)
            ) {
                TagLabel(i)
            }
        }
    }
}

@Composable
fun TagLabel(tag: ClimbTag) {
    Row (
        modifier = Modifier
            .height(24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(tag.type.imageResourceId),
            contentDescription = null,
            modifier = Modifier.size(18.dp)
        )
        Text(
            text = tag.name,
            fontSize = 10.sp,
            modifier = Modifier.padding(start = 5.dp)
        )
    }
}

// Show 0-3 stars depending on rating
@Composable
fun RatingStars(rating: Int, modifier: Modifier) {
    Row(
        modifier = modifier
    ) {
        for (i in 1..3) {
            if (i <= rating) {
                Icon(
                    modifier = Modifier.size(18.dp),
                    imageVector = Icons.Filled.Star,
                    tint = MaterialTheme.colorScheme.secondary,
                    contentDescription = null
                )
            }
        }
    }
}

// Completion status icon only
@Composable
fun CompletionStatusIcon(isComplete: Boolean) {

    // Change label based on completion status
    val labelColor = if (isComplete) Color(0xFF78A55A) else Color(0xFF8C8C8C)
    val labelIcon = if (isComplete) Icons.Filled.Check else Icons.Filled.MoreVert

    Surface(
        color = labelColor,
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(4.dp)
        ) {
            Icon(
                imageVector = labelIcon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .size(16.dp)
                    .rotate(if (isComplete) 0.0f else 90.0f)
            )
        }
    }
}

// Completion status icon and text
@Composable
fun CompletionStatusLabel(isComplete: Boolean) {

    // Change label based on completion status
    val labelColor = if (isComplete) Color(0xFF78A55A) else Color(0xFF8C8C8C)
    val labelIcon = if (isComplete) Icons.Filled.Check else Icons.Filled.MoreVert
    val labelText = if (isComplete) "Complete" else "Incomplete"

    Surface(
        color = labelColor,
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 6.dp, vertical = 4.dp)
        ) {
            Icon(
                imageVector = labelIcon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .size(18.dp)
                    .rotate(if (isComplete) 0.0f else 90.0f)
            )
            Text(
                text = labelText,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}