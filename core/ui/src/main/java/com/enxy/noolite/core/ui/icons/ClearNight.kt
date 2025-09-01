package com.enxy.noolite.core.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.enxy.noolite.core.ui.NooliteIcons

val NooliteIcons.ClearNight: ImageVector
    get() {
        if (_Clear_night != null) return _Clear_night!!

        _Clear_night = ImageVector.Builder(
            name = "ClearNight",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF000000))
            ) {
                moveTo(524f, 920f)
                quadToRelative(-84f, 0f, -157.5f, -32f)
                reflectiveQuadToRelative(-128f, -86.5f)
                reflectiveQuadToRelative(-86.5f, -128f)
                reflectiveQuadTo(120f, 516f)
                quadToRelative(0f, -146f, 93f, -257.5f)
                reflectiveQuadTo(450f, 120f)
                quadToRelative(-18f, 99f, 11f, 193.5f)
                reflectiveQuadTo(561f, 479f)
                reflectiveQuadToRelative(165.5f, 100f)
                reflectiveQuadTo(920f, 590f)
                quadToRelative(-26f, 144f, -138f, 237f)
                reflectiveQuadTo(524f, 920f)
                moveToRelative(0f, -80f)
                quadToRelative(88f, 0f, 163f, -44f)
                reflectiveQuadToRelative(118f, -121f)
                quadToRelative(-86f, -8f, -163f, -43.5f)
                reflectiveQuadTo(504f, 535f)
                reflectiveQuadToRelative(-97f, -138f)
                reflectiveQuadToRelative(-43f, -163f)
                quadToRelative(-77f, 43f, -120.5f, 118.5f)
                reflectiveQuadTo(200f, 516f)
                quadToRelative(0f, 135f, 94.5f, 229.5f)
                reflectiveQuadTo(524f, 840f)
                moveToRelative(-20f, -305f)
            }
        }.build()

        return _Clear_night!!
    }

private var _Clear_night: ImageVector? = null
