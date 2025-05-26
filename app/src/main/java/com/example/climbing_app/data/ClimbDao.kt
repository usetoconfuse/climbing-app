package com.example.climbing_app.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update


@Dao
interface ClimbDao {

    // Attempt table
    @Insert(entity = Attempt::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAttempt(attempt: Attempt)
    @Update(entity = Attempt::class)
    suspend fun updateAttempt(attempt: Attempt)
    @Delete(entity = Attempt::class)
    suspend fun deleteAttempt(attempt: Attempt)
    @Query("SELECT * FROM attempts ORDER BY date DESC")
    fun getAllAttempts(): LiveData<List<Attempt>>
}