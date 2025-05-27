package com.example.climbing_app

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.core.net.toUri
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.climbing_app.provider.ClimbContract
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDateTime

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).

@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.climbing_app", appContext.packageName)
    }
}
 */

// These tests require hard-coded id values from Firebase which cannot be fetched by the app
// So the content provider would fail in a real use case
@RunWith(AndroidJUnit4::class)
class ClimbProviderTest {

    private lateinit var context: Context
    private lateinit var resolver: ContentResolver

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        resolver = context.contentResolver

        val values = ContentValues().apply {
            put(ClimbContract.Climbs.COLUMN_USER_ID, "fake_firestore_id")
            put(ClimbContract.Climbs.COLUMN_CLIMB_ID, "fake_firestore_id")
            put(ClimbContract.Climbs.COLUMN_COMPLETED, false)
        }
        Log.d("setup", values.toString())
        val uri = resolver.insert(ClimbContract.Climbs.CONTENT_URI, values)
    }

    @Test
    fun testDeleteAttempt() {
        // Insert an attempt first
        val uri = ClimbContract.Climbs.CONTENT_URI
        val values = ContentValues().apply {
            put(ClimbContract.Climbs.COLUMN_USER_ID, "fake_firestore_id")
            put(ClimbContract.Climbs.COLUMN_CLIMB_ID, "fake_firestore_id")
            put(ClimbContract.Climbs.COLUMN_COMPLETED, true)
            put(ClimbContract.Climbs.COLUMN_DATE, LocalDateTime.now().toString())
        }
        val insertUri = resolver.insert(uri, values)
        assertNotNull("Insert failed", insertUri)

        // Delete the attempt
        val deleteCount = resolver.delete(insertUri!!, null, null)
        assertEquals("Delete failed", 1, deleteCount)

        // Attempt to query the deleted attempt
        val cursor = resolver.query(insertUri, null, null, null, null)
        assertNotNull(cursor)
        assertFalse("Attempt should be deleted", cursor!!.moveToFirst())
        cursor.close()
    }

    @Test(expected = IllegalArgumentException::class)
    fun testQueryInvalidUri() {
        resolver.query("content://com.example.climbing_app.provider/invalid".toUri(), null, null, null, null)
    }

    @After
    fun tearDown() {
        // clear the database if needed after test
    }
}