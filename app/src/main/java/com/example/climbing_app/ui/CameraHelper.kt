package com.example.climbing_app.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Objects


// Helper function that sets up the necessary launchers to capture a photo with external camera app
// Returns the functions to capture and save a photo using a passed URI setter function
@Composable
fun Context.prepareCamera(setTargetUri: (Uri) -> Unit): () -> Unit {

    // Create a cache file to store photo and get its URI
    val formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
    val timeStamp = LocalDateTime.now().format(formatter)
    val imageFileName = "JPEG_" + timeStamp + "_"

    val file = File.createTempFile(
        imageFileName, // prefix
        ".jpg", // suffix
        externalCacheDir // directory
    )

    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(this),
        "${packageName}.provider",
        file
    )

    // Launcher for external camera activity which saves photo to our created file
    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) {
        // Don't replace previous image if the user backed out of the camera app
        if (it) setTargetUri(uri)
    }

    // Prompt for camera permission when needed
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            cameraLauncher.launch(uri)
        } else {
            Toast.makeText(this,"Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    // Take photo using intents
    return {
        val permissionCheckResult =
            ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
            cameraLauncher.launch(uri)
        } else {
            // Request a permission
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }
}