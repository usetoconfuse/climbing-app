package com.example.climbing_app.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDateTime


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
)