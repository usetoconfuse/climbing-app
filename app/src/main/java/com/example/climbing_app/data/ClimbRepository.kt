package com.example.climbing_app.data

import androidx.lifecycle.LiveData


class ClimbRepository(private val climbDao: ClimbDao) {
    val allClimbs: LiveData<List<Climb>> = climbDao.getAllClimbs()
    suspend fun insert(climb: Climb) {
        climbDao.insert(climb)
    }
    suspend fun update(climb: Climb) {
        climbDao.update(climb)
    }
    suspend fun delete(climb: Climb) {
        climbDao.delete(climb)
    }
    fun getClimbItem(id: Int): LiveData<Climb> {
        return climbDao.getClimbItem(id)
    }
}