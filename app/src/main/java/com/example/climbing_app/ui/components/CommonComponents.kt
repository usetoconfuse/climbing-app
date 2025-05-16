package com.example.climbing_app.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.climbing_app.data.ClimbTag
import com.example.climbing_app.data.ClimbTagHolds
import com.example.climbing_app.data.ClimbTagIncline
import com.example.climbing_app.data.ClimbTagStyle

@Composable
fun TagListRow(style: ClimbTagStyle, holds: ClimbTagHolds, incline: ClimbTagIncline) {
    Row(
        modifier = Modifier.fillMaxWidth()
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
fun CompletedStatusIcon() {
    Surface(
        color = Color(0xFF78A55A),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(4.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.CheckCircle,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun CompletedStatusLabel() {
    Surface(
        color = Color(0xFF78A55A),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(4.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.CheckCircle,
                contentDescription = null,
                tint = Color.White
            )
            Text(
                text = "Complete",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}