package com.example.climbing_app.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

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
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
fun CompletedStatusLabel() {
    Surface(
        modifier = Modifier
            .height(50.dp)
            .width(150.dp)
            .padding(5.dp),
        color = Color(0xFF78A55A)
    ) {
        Text("Complete")
    }
}