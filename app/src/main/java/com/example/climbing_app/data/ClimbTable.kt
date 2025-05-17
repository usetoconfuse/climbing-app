package com.example.climbing_app.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Entity (tableName = "climbs")
data class Climb (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val imageResourceId: Int,
    val grade: String,
    val rating: Int = 0,
    val description: String,
    val attempts: Int = 0,
    val uploadDate: String = LocalDateTime.now().toString(),
    val style: ClimbTagStyle,
    val holds: ClimbTagHolds,
    val incline: ClimbTagIncline
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