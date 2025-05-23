package com.example.climbing_app.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.climbing_app.data.Attempt
import com.example.climbing_app.data.Climb
import com.example.climbing_app.data.ClimbDatabase
import com.example.climbing_app.data.ClimbRepository
import com.example.climbing_app.data.User
import kotlinx.coroutines.launch


class ClimbViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ClimbRepository
    val allUsers: LiveData<List<User>>
    val allClimbs: LiveData<List<Climb>>
    val allAttempts: LiveData<List<Attempt>>
    var currentUser: LiveData<Int>
    init {
        val climbDao = ClimbDatabase.getDatabase(application).climbDao()
        repository = ClimbRepository(climbDao)
        allUsers = repository.allUsers
        allClimbs = repository.allClimbs
        allAttempts = repository.allAttempts
        currentUser = MutableLiveData<Int>()
    }

    // User table
    fun insertUser(user: User) = viewModelScope.launch {
        repository.insertUser(user)
    }
    fun updateUser(user: User) = viewModelScope.launch {
        repository.insertUser(user)
    }
    fun deleteUser(user: User) = viewModelScope.launch {
        repository.deleteUser(user)
    }

    // Climb table
    fun insertClimb(climb: Climb) = viewModelScope.launch {
        repository.insertClimb(climb)
    }
    fun updateClimb(climb: Climb) = viewModelScope.launch {
        repository.insertClimb(climb)
    }
    fun deleteClimb(climb: Climb) = viewModelScope.launch {
        repository.deleteClimb(climb)
    }

    // Attempt table
    fun insertAttempt(attempt: Attempt) = viewModelScope.launch {
        repository.insertAttempt(attempt)
    }
    fun updateAttempt(attempt: Attempt) = viewModelScope.launch {
        repository.insertAttempt(attempt)
    }
    fun deleteAttempt(attempt: Attempt) = viewModelScope.launch {
        repository.deleteAttempt(attempt)
    }

    // Queries

    fun authenticateUser(username: String, password: String) = viewModelScope.launch {
        currentUser = repository.authenticateUser(username, password)
    }
    fun getClimb(climbId: Int) = viewModelScope.launch {
        repository.getClimb(climbId)
    }
    fun getAttemptsByClimb(climbId: Int) = viewModelScope.launch {
        repository.getAttemptsByClimb(climbId)
    }
}