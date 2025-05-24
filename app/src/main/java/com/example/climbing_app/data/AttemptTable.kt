package com.example.climbing_app.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Entity (
    tableName = "attempts",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = arrayOf("userId"),
            childColumns = arrayOf("userId"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Climb::class,
            parentColumns = arrayOf("climbId"),
            childColumns = arrayOf("climbId"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Attempt(
    @PrimaryKey(autoGenerate = true) val attemptId: Int = 0,
    val userId: Int,
    val climbId: Int,
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