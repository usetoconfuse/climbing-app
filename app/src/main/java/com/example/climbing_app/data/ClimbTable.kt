package com.example.climbing_app.data

import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.climbing_app.R

@Entity (tableName = "climbs")
data class Climb(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val imageResourceId: Int,
    val grade: String,
    val rating: Int,
    val description: String,
    val style: ClimbTagStyle,
    val holds: ClimbTagHolds,
    val incline: ClimbTagIncline
)

/* enum class ClimbTagStyle(
    val type: ClimbTagType,
) {
    Powerful(ClimbTagType.Style),
    Technical(ClimbTagType.Style),
    Dynamic(ClimbTagType.Style),
    Static(ClimbTagType.Style),
    Crimps(ClimbTagType.Holds),
    Jugs(ClimbTagType.Holds),
    Pinches(ClimbTagType.Holds),
    Slopers(ClimbTagType.Holds),
    Pockets(ClimbTagType.Holds),
    Slab(ClimbTagType.Incline),
    Wall(ClimbTagType.Incline),
    Overhang(ClimbTagType.Incline)
} */

enum class ClimbTagStyle: ClimbTag {
    Powerful,
    Technical,
    Dynamic,
    Static
    ;
    override val type: ClimbTagType = ClimbTagType.Style
}

enum class ClimbTagHolds: ClimbTag {
    Crimps,
    Jugs,
    Pinches,
    Slopers,
    Pockets
    ;
    override val type: ClimbTagType = ClimbTagType.Holds
}

enum class ClimbTagIncline: ClimbTag {
    Slab,
    Wall,
    Overhang
    ;
    override val type: ClimbTagType = ClimbTagType.Incline
}

interface ClimbTag {
    val name: String
    val type: ClimbTagType
}

enum class ClimbTagType(
    val imageResourceId: Int,
    val labelColor: Color
) {
    Style(R.drawable.tag_img_style, Color(0xFF9D70BC)),
    Holds(R.drawable.tag_img_holds, Color(0xFFBC7071)),
    Incline(R.drawable.tag_img_incline, Color(0xFF70AEBC))
}