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
import kotlinx.coroutines.launch


class ClimbViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ClimbRepository
    val allAttempts: LiveData<List<Attempt>>
    init {
        val roomDb = ClimbDatabase.getDatabase(application)
        val climbDao = roomDb.climbDao()
        repository = ClimbRepository(climbDao)
        allAttempts = repository.allAttempts
    }

    // Climb table
    /*
    fun insertClimb(climb: Climb) = viewModelScope.launch {
        repository.insertClimb(climb)
    }
    fun updateClimb(climb: Climb) = viewModelScope.launch {
        repository.insertClimb(climb)
    }
    fun deleteClimb(climb: Climb) = viewModelScope.launch {
        repository.deleteClimb(climb)
    }
    */

    // Firestore climbs collection
    fun insertClimb(climb: Climb) = repository.insertClimb(climb)
    fun getClimb(climbId: String): LiveData<Climb> {
        val result = MutableLiveData<Climb>()
        viewModelScope.launch {
            result.postValue(repository.getClimb(climbId))
        }
        return result
    }
    fun getFilteredClimbs(searchQuery: String): LiveData<List<Climb>> {
        val result = MutableLiveData<List<Climb>>()
        viewModelScope.launch {
            result.postValue(repository.getFilteredClimbs(searchQuery))
        }
        return result
    }



    // Local attempt table
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
    /*
    fun getClimb(climbId: Int) = viewModelScope.launch {
        repository.getClimb(climbId)
    }
    */
    fun getAttemptsByClimb(climbId: Int) = viewModelScope.launch {
        repository.getAttemptsByClimb(climbId)
    }
}