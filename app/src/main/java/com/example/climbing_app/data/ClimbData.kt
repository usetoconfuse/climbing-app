package com.example.climbing_app.data

import androidx.compose.ui.graphics.Color
import com.example.climbing_app.R

data class ClimbData(
    val name: String,
    val imageResourceId: Int,
    val grade: String,
    val rating: Int,
    val description: String,
    val tags: List<ClimbTag>
)

enum class ClimbTag(
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
}

enum class ClimbTagType(
    val imageResourceId: Int,
    val labelColor: Color
) {
    Style(R.drawable.tag_img_style, Color(0xFF9D70BC)),
    Holds(R.drawable.tag_img_holds, Color(0xFFBC7071)),
    Incline(R.drawable.tag_img_incline, Color(0xFF70AEBC))
}

