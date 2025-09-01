package com.enxy.noolite.core.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.enxy.noolite.core.ui.NooliteIcons

val NooliteIcons.AutoRenew: ImageVector
    get() {
        if (_AutoRenew != null) {
            return _AutoRenew!!
        }
        _AutoRenew = ImageVector.Builder(
            name = "AutoRenew",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(204f, 642f)
                quadToRelative(-22f, -38f, -33f, -78f)
                reflectiveQuadToRelative(-11f, -82f)
                quadToRelative(0f, -134f, 93f, -228f)
                reflectiveQuadToRelative(227f, -94f)
                horizontalLineToRelative(7f)
                lineToRelative(-64f, -64f)
                lineToRelative(56f, -56f)
                lineToRelative(160f, 160f)
                lineToRelative(-160f, 160f)
                lineToRelative(-56f, -56f)
                lineToRelative(64f, -64f)
                horizontalLineToRelative(-7f)
                quadToRelative(-100f, 0f, -170f, 70.5f)
                reflectiveQuadTo(240f, 482f)
                quadToRelative(0f, 26f, 6f, 51f)
                reflectiveQuadToRelative(18f, 49f)
                close()
                moveTo(481f, 920f)
                lineTo(321f, 760f)
                lineToRelative(160f, -160f)
                lineToRelative(56f, 56f)
                lineToRelative(-64f, 64f)
                horizontalLineToRelative(7f)
                quadToRelative(100f, 0f, 170f, -70.5f)
                reflectiveQuadTo(720f, 478f)
                quadToRelative(0f, -26f, -6f, -51f)
                reflectiveQuadToRelative(-18f, -49f)
                lineToRelative(60f, -60f)
                quadToRelative(22f, 38f, 33f, 78f)
                reflectiveQuadToRelative(11f, 82f)
                quadToRelative(0f, 134f, -93f, 228f)
                reflectiveQuadToRelative(-227f, 94f)
                horizontalLineToRelative(-7f)
                lineToRelative(64f, 64f)
                close()
            }
        }.build()

        return _AutoRenew!!
    }

@Suppress("ObjectPropertyName")
private var _AutoRenew: ImageVector? = null
