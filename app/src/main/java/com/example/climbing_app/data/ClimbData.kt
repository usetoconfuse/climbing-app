package com.example.climbing_app.data

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
    Jugs,
    Crimps,
    Pinches,
    Slopers,
    Pockets
    ;
    override val type: ClimbTagType = ClimbTagType.Holds
}

enum class ClimbTagIncline: ClimbTag {
    Wall,
    Slab,
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
) {
    Style(R.drawable.tag_img_style),
    Holds(R.drawable.tag_img_holds),
    Incline(R.drawable.tag_img_incline)
}