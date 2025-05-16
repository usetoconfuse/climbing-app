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
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(climb: Climb)
    @Update
    suspend fun update(climb: Climb)
    @Delete
    suspend fun delete(climb: Climb)
    @Query("SELECT * FROM climbs WHERE id = :id")
    fun getClimbItem(id: Int): LiveData<Climb>
    @Query("SELECT * FROM climbs")
    fun getAllClimbs(): LiveData<List<Climb>>
}