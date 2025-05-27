package com.example.climbing_app.data

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.Firebase
import com.google.firebase.firestore.Filter.and
import com.google.firebase.firestore.Filter.greaterThanOrEqualTo
import com.google.firebase.firestore.Filter.lessThanOrEqualTo
import com.google.firebase.firestore.Filter.or
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.google.firebase.storage.storage
import kotlinx.coroutines.tasks.await


class ClimbRepository(private val climbDao: ClimbDao) {
    val allAttempts: LiveData<List<Attempt>> = climbDao.getAllAttempts()
    var allClimbs: MutableLiveData<List<Climb>> = MutableLiveData<List<Climb>>(emptyList())

    private val db = Firebase.firestore
    private val storage = Firebase.storage
    private val climbCollection = db.collection("climbs")
    private var filteredClimbCollectionQuery: Query = climbCollection.orderBy("uploadDate", Query.Direction.DESCENDING)

    // Add a snapshot listener to update the climbs list when a climb is uploaded
    private var snapshotListener = filteredClimbCollectionQuery.addSnapshotListener { snapshot, e ->
        if (e != null) {
            Log.w(TAG, "Listen failed.", e)
            return@addSnapshotListener
        }

        // Detect if the upload originates from this device or the server
        val source = if (snapshot != null && snapshot.metadata.hasPendingWrites()) {
            "Local"
        } else {
            "Server"
        }

        if (snapshot != null) {
            Log.d(TAG, "$source data: ${snapshot.toObjects<Climb>()}")
            allClimbs.value = snapshot.toObjects<Climb>()
        } else {
            Log.d(TAG, "$source data: null")
        }
    }

    // Add a climb to the Firestore climbs collection
    suspend fun insertClimb(climb: Climb) {
        if (climb.imageLocation != null) {
            // Upload the image to firebase storage
            val storageRef = storage.reference
            val localUri = climb.imageLocation!!.toUri()
            val pathString = "images/${localUri.lastPathSegment}"
            val imageRef = storageRef.child(pathString)

            imageRef.putFile(localUri)
            // Register observers to listen for when the download is done or if it fails
                .addOnSuccessListener {
                    Log.d(TAG, "Uploaded image $pathString")
                    climb.imageLocation = pathString
                }
                .addOnFailureListener {
                    Log.w(TAG, "Failed to upload image $pathString")
                }.await()
        }

        // Add the climb to the firestore collection with a reference to the uploaded image
        climbCollection.add(climb)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot written with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }
    // Get a specific climb from the collection by its generated ID
    suspend fun getClimb(climbId: String): Climb? {
        val thisClimb = climbCollection.document(climbId).get().await()
        return thisClimb.toObject<Climb>()
    }
    // Get the list of uploaded climbs based on a filter, empty filter returns all climbs
    fun filterClimbs(searchQuery: String) {
        filteredClimbCollectionQuery = db.collection("climbs").where(
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
                    greaterThanOrEqualTo("uploader", searchQuery),
                    lessThanOrEqualTo("uploader", searchQuery+"\uf8ff")
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
        ).orderBy("uploadDate", Query.Direction.DESCENDING)
        // Replace snapshot listener for new query
        snapshotListener.remove()
        snapshotListener = filteredClimbCollectionQuery.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            val source = if (snapshot != null && snapshot.metadata.hasPendingWrites()) {
                "Local"
            } else {
                "Server"
            }

            if (snapshot != null) {
                Log.d(TAG, "$source data: ${snapshot.toObjects<Climb>()}")
                allClimbs.value = snapshot.toObjects<Climb>()
            } else {
                Log.d(TAG, "$source data: null")
            }
        }
    }
    suspend fun getClimbImage(climb: Climb): Uri {
        var imageUri = Uri.EMPTY
        if (climb.imageLocation != null) {
            // Download the image
            val storageRef = Firebase.storage.reference
            val imageRef = storageRef.child(climb.imageLocation!!)
            imageRef.downloadUrl
                .addOnSuccessListener {
                    Log.d(TAG, "Successfully got image url $it")
                    imageUri = it
                }
                .addOnFailureListener {
                    Log.w(TAG, "Failed to get image url: ${it.message ?: it.toString()}")
                }.await()
        } else {
            // Default image if none uploaded
            imageUri = "android.resource://com.example.climbing_app/drawable/img_placeholder".toUri()
        }
        return imageUri
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
}