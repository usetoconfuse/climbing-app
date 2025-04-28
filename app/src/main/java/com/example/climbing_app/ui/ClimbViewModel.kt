package com.example.climbing_app.ui

import androidx.lifecycle.ViewModel
import com.example.climbing_app.data.ClimbData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ClimbViewModel : ViewModel() {
    private val _climbList = MutableStateFlow<List<ClimbData>>(emptyList())
    val climbList = _climbList.asStateFlow()

    fun addClimb(climb : ClimbData) {
        // TODO implement this
        _climbList.value += climb
    }
}