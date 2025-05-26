package com.example.climbing_app.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Entity (
    tableName = "attempts"
)
data class Attempt(
    @PrimaryKey(autoGenerate = true) val attemptId: Int = 0,
    val userId: String = Firebase.auth.currentUser!!.uid,
    val climbId: String,
    val completed: Boolean,
    val date: String = LocalDateTime.now().toString()
) {
    // Get the formatted upload date
    fun formattedUploadDate(): String {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        return LocalDateTime.parse(this.date).format(formatter)
    }
    // Get the formatted upload time
    fun formattedUploadTime(): String {
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        return LocalDateTime.parse(this.date).format(formatter)
    }
}