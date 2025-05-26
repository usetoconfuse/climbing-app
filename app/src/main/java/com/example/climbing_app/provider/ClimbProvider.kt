package com.example.climbing_app.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.example.climbing_app.data.Attempt
import com.example.climbing_app.data.ClimbDao
import com.example.climbing_app.data.ClimbDatabase


class ClimbProvider : ContentProvider() {
    private lateinit var climbDao: ClimbDao

    override fun onCreate(): Boolean {
        climbDao = ClimbDatabase.getDatabase(context!!).climbDao()
        return true
    }

    // UriMatcher
    companion object {
        private const val ATTEMPTS = 100
        private const val ATTEMPT_ID = 101

        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(ClimbContract.AUTHORITY, ClimbContract.Climbs.PATH_CLIMBS, ATTEMPTS)
            addURI(ClimbContract.AUTHORITY, "${ClimbContract.Climbs.PATH_CLIMBS}/#", ATTEMPT_ID)
        }
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor {
        val match = uriMatcher.match(uri)
        return when (match) {
            ATTEMPTS -> climbDao.getAllAttemptsCursor()
            ATTEMPT_ID -> {
                val id = ContentUris.parseId(uri)
                climbDao.getAttemptItemCursor(id.toInt())
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri {
        val newRowId = when (uriMatcher.match(uri)) {
            ATTEMPTS -> {
                val attempt = Attempt(
                    userId = values?.getAsString(ClimbContract.Climbs.COLUMN_USER_ID) ?: "",
                    climbId = values?.getAsString(ClimbContract.Climbs.COLUMN_CLIMB_ID) ?: "",
                    completed = values?.getAsBoolean(ClimbContract.Climbs.COLUMN_COMPLETED) == true,
                    date = values?.getAsString(ClimbContract.Climbs.COLUMN_DATE) ?: ""
                )
                climbDao.insertAttemptItem(attempt)
            }
            else -> throw IllegalArgumentException("Invalid URI for insert: $uri")
        }
        context?.contentResolver?.notifyChange(uri, null)
        return ContentUris.withAppendedId(uri, newRowId)
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val context = context ?: return 0  // Check if context is null
        val match = uriMatcher.match(uri)
        return when (match) {
            ATTEMPT_ID -> {
                val attemptId = ContentUris.parseId(uri).toInt()
                // Retrieve the attempt to be deleted
                val cursor = climbDao.getAttemptItemCursor(attemptId)
                if (cursor.moveToFirst()) {
                    val attempt = Attempt(
                        attemptId = cursor.getInt(cursor.getColumnIndexOrThrow("attemptId")),
                        userId = cursor.getString(cursor.getColumnIndexOrThrow("userId")),
                        climbId = cursor.getString(cursor.getColumnIndexOrThrow("climbId")),
                        completed = cursor.getInt(cursor.getColumnIndexOrThrow("completed")) > 0,
                        date = cursor.getString(cursor.getColumnIndexOrThrow("date"))
                    )
                    cursor.close()
                    // Use the DAO to delete the attempt
                    climbDao.deleteAttemptItem(attempt)
                } else {
                    cursor.close()
                    0  // No attempt found to delete
                }
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        throw UnsupportedOperationException("Update operation is not supported")
    }

    override fun getType(uri: Uri): String {
        return when (uriMatcher.match(uri)) {
            ATTEMPTS -> ClimbContract.Climbs.CONTENT_TYPE
            ATTEMPT_ID -> ClimbContract.Climbs.CONTENT_ITEM_TYPE
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }
}