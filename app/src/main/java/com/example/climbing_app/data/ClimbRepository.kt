package com.example.climbing_app.data

import androidx.lifecycle.LiveData


class ClimbRepository(private val climbDao: ClimbDao) {
    val allUsers: LiveData<List<User>> = climbDao.getAllUsers()
    val allClimbs: LiveData<List<Climb>> = climbDao.getAllClimbs()
    val allAttempts: LiveData<List<Attempt>> = climbDao.getAllAttempts()

    // User table
    suspend fun insertUser(user: User) {
        climbDao.insertUser(user)
    }
    suspend fun updateUser(user: User) {
        climbDao.updateUser(user)
    }
    suspend fun deleteUser(user: User) {
        climbDao.deleteUser(user)
    }

    // Climb table
    suspend fun insertClimb(climb: Climb) {
        climbDao.insertClimb(climb)
    }
    suspend fun updateClimb(climb: Climb) {
        climbDao.updateClimb(climb)
    }
    suspend fun deleteClimb(climb: Climb) {
        climbDao.deleteClimb(climb)
    }

    // Attempt table
    suspend fun insertAttempt(attempt: Attempt) {
        climbDao.insertAttempt(attempt)
    }
    suspend fun updateAttempt(attempt: Attempt) {
        climbDao.updateAttempt(attempt)
    }
    suspend fun deleteAttempt(attempt: Attempt) {
        climbDao.deleteAttempt(attempt)
    }

    // Queries
    fun authenticateUser(username: String, password: String): LiveData<Int> {
        return climbDao.authenticateUser(username, password)
    }

    fun getClimb(id: Int): LiveData<Climb> {
        return climbDao.getClimb(id)
    }

    fun getAttemptsByClimb(climbId: Int): LiveData<List<Attempt>> {
        return climbDao.getAttemptsByClimb(climbId)
    }
}