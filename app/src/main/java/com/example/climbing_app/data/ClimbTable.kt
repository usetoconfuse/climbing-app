package com.example.climbing_app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "climbs")
data class Climb(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val imageResourceId: Int,
    val grade: String,
    val rating: Int,
    val description: String,
    val attempts: Int,
    val uploadDate: String,
    val style: ClimbTagStyle,
    val holds: ClimbTagHolds,
    val incline: ClimbTagIncline
)