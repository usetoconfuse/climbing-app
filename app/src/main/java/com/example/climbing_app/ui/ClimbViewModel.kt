package com.example.climbing_app.ui

import android.app.Application
import android.net.Uri
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
    val allClimbs: LiveData<List<Climb>>
    init {
        val roomDb = ClimbDatabase.getDatabase(application)
        val climbDao = roomDb.climbDao()
        repository = ClimbRepository(climbDao)
        allAttempts = repository.allAttempts
        allClimbs = repository.allClimbs
    }

    // Firestore climbs collection
    fun insertClimb(climb: Climb) = viewModelScope.launch {
        repository.insertClimb(climb)
    }
    fun getClimb(climbId: String): LiveData<Climb> {
        val result = MutableLiveData<Climb>()
        viewModelScope.launch {
            result.postValue(repository.getClimb(climbId))
        }
        return result
    }
    fun filterClimbs(searchQuery: String) = viewModelScope.launch {
        repository.filterClimbs(searchQuery)
    }
    fun getClimbImage(climb: Climb): MutableLiveData<Uri> {
        val result = MutableLiveData<Uri>()
        viewModelScope.launch {
            result.postValue(repository.getClimbImage(climb))
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
}