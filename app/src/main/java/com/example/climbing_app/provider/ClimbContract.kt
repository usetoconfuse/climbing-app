package com.example.climbing_app.provider

import android.net.Uri
import androidx.core.net.toUri

object ClimbContract {
    const val AUTHORITY = "com.example.climbing_app.provider"
    val BASE_CONTENT_URI: Uri = "content://$AUTHORITY".toUri()

    object Climbs {
        const val PATH_CLIMBS = "Climbs"
        val CONTENT_URI: Uri = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_CLIMBS)

        const val CONTENT_TYPE = "vnd.android.cursor.dir/vnd.$AUTHORITY.$PATH_CLIMBS"
        const val CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.$AUTHORITY.$PATH_CLIMBS"

        const val COLUMN_ID = "attemptId"
        const val COLUMN_USER_ID = "userId"
        const val COLUMN_CLIMB_ID = "climbId"
        const val COLUMN_COMPLETED = "completed"
        const val COLUMN_DATE = "date"
    }
}