package com.example.climbing_app.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.climbing_app.data.Climb
import com.example.climbing_app.data.ClimbDatabase
import com.example.climbing_app.data.ClimbRepository
import kotlinx.coroutines.launch

class ClimbViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ClimbRepository
    val allClimbs: LiveData<List<Climb>>
    init {
        val climbDao = ClimbDatabase.getDatabase(application).climbDao()
        repository = ClimbRepository(climbDao)
        allClimbs = repository.allClimbs
    }
    fun insert(climb: Climb) = viewModelScope.launch {
        repository.insert(climb)
    }
    fun update(climb: Climb) = viewModelScope.launch {
        repository.insert(climb)
    }
    fun delete(climb: Climb) = viewModelScope.launch {
        repository.delete(climb)
    }
    fun getItem(id: Int) = viewModelScope.launch {
        repository.getClimbItem(id)
    }
}