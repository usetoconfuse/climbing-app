package com.example.climbing_app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database (
    entities = [
        Attempt::class
               ],
    version = 1,
    exportSchema = false
)
abstract class ClimbDatabase:RoomDatabase() {
    abstract fun climbDao(): ClimbDao
    companion object {
        @Volatile
        private var INSTANCE: ClimbDatabase? = null

        fun getDatabase(context: Context): ClimbDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ClimbDatabase::class.java,
                    "Climbs"
                ).build()
                INSTANCE = instance
                // return
                instance
            }
        }
    }
}