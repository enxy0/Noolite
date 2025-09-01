package com.enxy.noolite.core.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.enxy.noolite.core.ui.NooliteIcons

val NooliteIcons.LightOff: ImageVector
    get() {
        if (_LightOff != null) {
            return _LightOff!!
        }
        _LightOff = ImageVector.Builder(
            name = "LightOff",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(480f, 880f)
                quadToRelative(-33f, 0f, -56.5f, -23.5f)
                reflectiveQuadTo(400f, 800f)
                horizontalLineToRelative(160f)
                quadToRelative(0f, 33f, -23.5f, 56.5f)
                reflectiveQuadTo(480f, 880f)
                moveToRelative(0f, -720f)
                quadToRelative(-44f, 0f, -81.5f, 15.5f)
                reflectiveQuadTo(332f, 218f)
                lineToRelative(-58f, -56f)
                quadToRelative(41f, -38f, 93.5f, -60f)
                reflectiveQuadTo(480f, 80f)
                quadToRelative(125f, 0f, 212.5f, 87.5f)
                reflectiveQuadTo(780f, 380f)
                quadToRelative(0f, 71f, -25f, 121.5f)
                reflectiveQuadTo(698f, 584f)
                lineToRelative(-56f, -56f)
                quadToRelative(21f, -23f, 39.5f, -59f)
                reflectiveQuadToRelative(18.5f, -89f)
                quadToRelative(0f, -92f, -64f, -156f)
                reflectiveQuadToRelative(-156f, -64f)
                moveToRelative(368f, 688f)
                lineToRelative(-57f, 57f)
                lineToRelative(-265f, -265f)
                lineTo(330f, 640f)
                quadToRelative(-69f, -41f, -109.5f, -110f)
                reflectiveQuadTo(180f, 380f)
                quadToRelative(0f, -20f, 2.5f, -39f)
                reflectiveQuadToRelative(7.5f, -37f)
                lineTo(56f, 168f)
                lineToRelative(56f, -56f)
                close()
                moveTo(354f, 560f)
                horizontalLineToRelative(92f)
                lineTo(260f, 374f)
                verticalLineToRelative(6f)
                quadToRelative(0f, 54f, 24.5f, 101f)
                reflectiveQuadToRelative(69.5f, 79f)
                moveToRelative(292f, 120f)
                verticalLineToRelative(80f)
                lineTo(320f, 760f)
                verticalLineToRelative(-80f)
                close()
            }
        }.build()

        return _LightOff!!
    }

@Suppress("ObjectPropertyName")
private var _LightOff: ImageVector? = null
