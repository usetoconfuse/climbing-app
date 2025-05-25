package com.example.climbing_app.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Entity (
    tableName = "climbs"
)
data class Climb(
    @PrimaryKey(autoGenerate = true) val climbId: Int = 0,
    val userId: String = "",
    val name: String = "",
    val imageUri: String = "",
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