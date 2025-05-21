package com.example.climbing_app.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.climbing_app.R
import com.example.climbing_app.data.Climb
import com.example.climbing_app.data.ClimbTagHolds
import com.example.climbing_app.data.ClimbTagIncline
import com.example.climbing_app.data.ClimbTagStyle
import com.example.climbing_app.data.ClimbTagType
import com.example.climbing_app.ui.ClimbViewModel
import com.example.climbing_app.ui.components.ClimbingMinorTopAppBar
import com.example.climbing_app.ui.prepareCamera


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadClimbScreen(climbViewModel: ClimbViewModel, navController: NavController) {
    Scaffold(
        topBar = { ClimbingMinorTopAppBar("New Climb", navController) },
        modifier = Modifier.fillMaxSize()
    ) {innerPadding ->
        UploadClimbContent(
            climbViewModel = climbViewModel,
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun UploadClimbContent(
    climbViewModel: ClimbViewModel,
    navController: NavController,
    modifier: Modifier
) {
    // Get context for toast
    val context = LocalContext.current

    // Input values for new climb
    var name by rememberSaveable { mutableStateOf("") }
    var grade by rememberSaveable { mutableStateOf("") }
    var rating by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var style by rememberSaveable { mutableStateOf(ClimbTagStyle.Powerful) }
    var holds by rememberSaveable { mutableStateOf(ClimbTagHolds.Jugs) }
    var incline by rememberSaveable { mutableStateOf(ClimbTagIncline.Wall) }
    var capturedImageUri by rememberSaveable { mutableStateOf<Uri>(Uri.EMPTY) }

    val takePhoto = context.prepareCamera { capturedImageUri = it }

    // Painter resource for image preview
    val previewPainter = rememberAsyncImagePainter(capturedImageUri)

    // Upload
    fun uploadClimb() {
        // Display toast and do not upload if any input field is empty
        if (listOf(name, grade, rating, description).any { x -> x.isEmpty() }) {
            Toast.makeText(
                context,
                "Cannot upload climb with empty fields",
                Toast.LENGTH_SHORT
            ).show()
        }
        else {
            // Upload the climb
            val newClimb = Climb(
                name = name,
                imageUri = if (capturedImageUri.path?.isNotEmpty() == true){
                    capturedImageUri.toString()
                } else {
                    // Default image if none uploaded
                    "android.resource://com.example.climbing_app/drawable/img_placeholder"
                },
                grade = "V$grade",
                rating = Integer.parseInt(rating),
                description = description,
                style = style,
                holds = holds,
                incline = incline
            )

            climbViewModel.insert(newClimb)
            Toast.makeText(
                context,
                "${newClimb.name} uploaded successfully",
                Toast.LENGTH_SHORT
            ).show()
            navController.popBackStack()
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                Modifier.weight(1.5f)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Name TextField
                    OutlinedTextField(
                        modifier = Modifier.weight(2.5f),
                        label = { Text("Name") },
                        singleLine = true,
                        value = name,
                        onValueChange = {
                            name = it.take(30)
                        }
                    )
                    // Add photo button
                    FloatingActionButton(
                        modifier = Modifier
                            .padding(start = 8.dp, top = 12.dp)
                            .weight(1.0f)
                            .aspectRatio(1f),
                        onClick = { takePhoto() }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.photo_camera),
                            contentDescription = null
                        )
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    // Grade TextField
                    OutlinedTextField(
                        modifier = Modifier.weight(1.0f),
                        label = { Text("V-grade") },
                        singleLine = true,
                        placeholder = { Text("0-17") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        value = grade,
                        onValueChange = {
                            if (it.isEmpty()) grade = it
                            else if (it.isDigitsOnly()) {
                                val intVal = Integer.parseInt(it)
                                if (intVal < 0) grade = "0"
                                else if (intVal > 17) grade = "17"
                                else grade = intVal.toString()
                            }
                        }
                    )
                    // Rating TextField
                    OutlinedTextField(
                        modifier = Modifier.weight(1.0f),
                        label = { Text("Rating") },
                        singleLine = true,
                        placeholder = { Text("0-3") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        value = rating,
                        onValueChange = {
                            if (it.isEmpty()) rating = it
                            else if (it.isDigitsOnly()) {
                                val intVal = Integer.parseInt(it)
                                if (intVal < 0) rating = "0"
                                else if (intVal > 3) rating = "3"
                                else rating = intVal.toString()
                            }
                        }
                    )
                }
            }
            Image(
                // Show thumbnail if a photo has been taken, otherwise fallback to placeholder
                // BUG: image errors if you rotate the screen whilst in the camera app
                painter = if (capturedImageUri != Uri.EMPTY) previewPainter
                else painterResource(R.drawable.img_placeholder),
                contentDescription = "Picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .weight(1.0f)
                    .padding(top = 8.dp, start = 16.dp)
                    .aspectRatio(1f)
            )
        }
        // Description TextField
        OutlinedTextField(
            label = { Text("Description") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            value = description,
            onValueChange = {
                if (description.length <= 100) description = it
            }
        )

        // Label and segmented button row for style tag
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 12.dp)
        ) {
            Icon(
                painter = painterResource(ClimbTagType.Style.imageResourceId),
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = "Style",
                modifier = Modifier.padding(start = 6.dp)
            )
        }
        SingleChoiceSegmentedButtonRow(
            Modifier.fillMaxWidth()
        ) {
            ClimbTagStyle.entries.forEach { buttonStyle ->
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(
                        index = buttonStyle.ordinal,
                        count = ClimbTagStyle.entries.size
                    ),
                    onClick = { style = buttonStyle },
                    selected = style == buttonStyle,
                    icon = {},
                    label = { Text(text = buttonStyle.name, fontSize = 11.sp) }
                )
            }
        }

        // Label and segmented button row for holds tag
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 12.dp)
        ) {
            Icon(
                painter = painterResource(ClimbTagType.Holds.imageResourceId),
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = "Holds",
                modifier = Modifier.padding(start = 6.dp)
            )
        }
        SingleChoiceSegmentedButtonRow(
            Modifier.fillMaxWidth()
        ) {
            ClimbTagHolds.entries.forEach { buttonHolds ->
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(
                        index = buttonHolds.ordinal,
                        count = ClimbTagHolds.entries.size
                    ),
                    onClick = { holds = buttonHolds },
                    selected = holds == buttonHolds,
                    icon = {},
                    label = { Text(text = buttonHolds.name, fontSize = 11.sp) }
                )
            }
        }

        // Label and segmented button row for incline tag
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 12.dp)
        ) {
            Icon(
                painter = painterResource(ClimbTagType.Incline.imageResourceId),
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = "Incline",
                modifier = Modifier.padding(start = 6.dp)
            )
        }
        SingleChoiceSegmentedButtonRow(
            Modifier.fillMaxWidth()
        ) {
            ClimbTagIncline.entries.forEach { buttonIncline ->
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(
                        index = buttonIncline.ordinal,
                        count = ClimbTagIncline.entries.size
                    ),
                    onClick = { incline = buttonIncline },
                    selected = incline == buttonIncline,
                    icon = {},
                    label = { Text(text = buttonIncline.name, fontSize = 11.sp) }
                )
            }
        }

        // Upload button
        Button(
            modifier = Modifier.padding(top = 24.dp),
            onClick = { uploadClimb() }
        ) {
            Text("UPLOAD")
        }
    }
}