package com.example.climbing_app.data

import androidx.compose.ui.graphics.Color
import com.example.climbing_app.R

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