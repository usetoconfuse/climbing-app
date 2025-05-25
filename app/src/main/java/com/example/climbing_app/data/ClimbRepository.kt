package com.example.climbing_app.data

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import com.google.firebase.Firebase
import com.google.firebase.firestore.Filter.and
import com.google.firebase.firestore.Filter.greaterThanOrEqualTo
import com.google.firebase.firestore.Filter.lessThanOrEqualTo
import com.google.firebase.firestore.Filter.or
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.tasks.await


class ClimbRepository(private val climbDao: ClimbDao) {
    val allAttempts: LiveData<List<Attempt>> = climbDao.getAllAttempts()

    private val db = Firebase.firestore
    private val climbCollection = db.collection("climbs")

    /*
    // Local climb table
    suspend fun insertClimb(climb: Climb) {
        climbDao.insertClimb(climb)
    }
    suspend fun updateClimb(climb: Climb) {
        climbDao.updateClimb(climb)
    }
    suspend fun deleteClimb(climb: Climb) {
        climbDao.deleteClimb(climb)
    }
    */

    // Firestore climbs collection
    fun insertClimb(climb: Climb) {
        climbCollection.add(climb)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot written with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }
    suspend fun getClimb(climbId: String): Climb? {
        val thisClimb = climbCollection.document(climbId).get().await()
        return thisClimb.toObject<Climb>()
    }
    suspend fun getFilteredClimbs(searchQuery: String): List<Climb> {
        // Filter only items whose name, grade or tags contain the query string (case-sensitive)
        val climbCollection = db.collection("climbs")
        val climbFilterQuery = climbCollection.where(
            or(
            and(
                greaterThanOrEqualTo("name", searchQuery),
                lessThanOrEqualTo("name", searchQuery+"\uf8ff")
            ),
            and(
                greaterThanOrEqualTo("grade", searchQuery),
                lessThanOrEqualTo("grade", searchQuery+"\uf8ff")
            ),
            and(
                greaterThanOrEqualTo("style", searchQuery),
                lessThanOrEqualTo("style", searchQuery+"\uf8ff")
            ),
            and(
                greaterThanOrEqualTo("holds", searchQuery),
                lessThanOrEqualTo("holds", searchQuery+"\uf8ff")
            ),
            and(
                greaterThanOrEqualTo("incline", searchQuery),
                lessThanOrEqualTo("incline", searchQuery+"\uf8ff")
            )
        )
        )
        val snapshot = climbFilterQuery.get().await()
        return snapshot.toObjects<Climb>()
    }


    // Local attempt table
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
    /*
    fun getClimb(id: Int): LiveData<Climb> {
        return climbDao.getClimb(id)
    }
    */
    fun getAttemptsByClimb(climbId: Int): LiveData<List<Attempt>> {
        return climbDao.getAttemptsByClimb(climbId)
    }
}