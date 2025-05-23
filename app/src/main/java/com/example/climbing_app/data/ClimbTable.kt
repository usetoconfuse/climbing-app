package com.example.climbing_app.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Entity (
    tableName = "climbs",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = arrayOf("userId"),
            childColumns = arrayOf("userId"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Climb(
    @PrimaryKey(autoGenerate = true) val climbId: Int = 0,
    val userId: Int,
    val name: String,
    val imageUri: String,
    val grade: String,
    val rating: Int = 0,
    val description: String,
    val uploadDate: String = LocalDateTime.now().toString(),
    val style: ClimbTagStyle,
    val holds: ClimbTagHolds,
    val incline: ClimbTagIncline
) {
    // Get the upload date as LocalDateTime
    fun getUploadDateTime(): LocalDateTime {
        return LocalDateTime.parse(this.uploadDate)
    }

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