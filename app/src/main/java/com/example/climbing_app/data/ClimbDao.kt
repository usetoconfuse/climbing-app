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

    // User table
    @Insert(entity = User::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: User)
    @Update(entity = User::class)
    suspend fun updateUser(user: User)
    @Delete(entity = User::class)
    suspend fun deleteUser(user: User)
    @Query("SELECT * FROM users")
    fun getAllUsers(): LiveData<List<User>>

    // Climb table
    @Insert(entity = Climb::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertClimb(climb: Climb)
    @Update(entity = Climb::class)
    suspend fun updateClimb(climb: Climb)
    @Delete(entity = Climb::class)
    suspend fun deleteClimb(climb: Climb)
    @Query("SELECT * FROM climbs")
    fun getAllClimbs(): LiveData<List<Climb>>

    // Attempt table
    @Insert(entity = Attempt::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAttempt(attempt: Attempt)
    @Update(entity = Attempt::class)
    suspend fun updateAttempt(attempt: Attempt)
    @Delete(entity = Attempt::class)
    suspend fun deleteAttempt(attempt: Attempt)
    @Query("SELECT * FROM attempts")
    fun getAllAttempts(): LiveData<List<Attempt>>

    // Queries
    @Query("SELECT userId FROM users WHERE username = :username AND password = :password")
    fun authenticateUser(username: String, password: String): LiveData<Int>

    @Query("SELECT * FROM climbs WHERE climbId = :climbId")
    fun getClimb(climbId: Int): LiveData<Climb>

    @Query("SELECT * FROM attempts WHERE climbId = :climbId")
    fun getAttemptsByClimb(climbId: Int): LiveData<List<Attempt>>
}