package com.example.climbing_app.data

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentId
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// Climb object stored to Firestore
data class Climb(
    @set:DocumentId var climbId: String? = null,
    val name: String = "",
    val uploader: String = Firebase.auth.currentUser?.displayName.toString(),
    var imageLocation: String? = null,
    val grade: String = "",
    val rating: Int = 0,
    val description: String = "",
    val uploadDate: String = LocalDateTime.now().toString(),
    val style: ClimbTagStyle = ClimbTagStyle.Powerful,
    val holds: ClimbTagHolds = ClimbTagHolds.Jugs,
    val incline: ClimbTagIncline = ClimbTagIncline.Overhang
) {
    // Get the formatted upload date
    fun formattedUploadDate(): String {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        return LocalDateTime.parse(this.uploadDate).format(formatter)
    }
    // Get the formatted upload time
    fun formattedUploadTime(): String {
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        return LocalDateTime.parse(this.uploadDate).format(formatter)
    }
}