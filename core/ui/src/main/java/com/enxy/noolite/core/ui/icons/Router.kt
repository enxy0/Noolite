package com.enxy.noolite.core.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.enxy.noolite.core.ui.NooliteIcons

val NooliteIcons.Router: ImageVector
    get() {
        if (_Router != null) {
            return _Router!!
        }
        _Router = ImageVector.Builder(
            name = "MaterialSymbolsOutlinedRouter",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(200f, 840f)
                quadToRelative(-33f, 0f, -56.5f, -23.5f)
                reflectiveQuadTo(120f, 760f)
                verticalLineToRelative(-160f)
                quadToRelative(0f, -33f, 23.5f, -56.5f)
                reflectiveQuadTo(200f, 520f)
                horizontalLineToRelative(400f)
                verticalLineToRelative(-160f)
                horizontalLineToRelative(80f)
                verticalLineToRelative(160f)
                horizontalLineToRelative(80f)
                quadToRelative(33f, 0f, 56.5f, 23.5f)
                reflectiveQuadTo(840f, 600f)
                verticalLineToRelative(160f)
                quadToRelative(0f, 33f, -23.5f, 56.5f)
                reflectiveQuadTo(760f, 840f)
                close()
                moveTo(200f, 760f)
                horizontalLineToRelative(560f)
                verticalLineToRelative(-160f)
                lineTo(200f, 600f)
                close()
                moveTo(280f, 720f)
                quadToRelative(17f, 0f, 28.5f, -11.5f)
                reflectiveQuadTo(320f, 680f)
                reflectiveQuadToRelative(-11.5f, -28.5f)
                reflectiveQuadTo(280f, 640f)
                reflectiveQuadToRelative(-28.5f, 11.5f)
                reflectiveQuadTo(240f, 680f)
                reflectiveQuadToRelative(11.5f, 28.5f)
                reflectiveQuadTo(280f, 720f)
                moveToRelative(140f, 0f)
                quadToRelative(17f, 0f, 28.5f, -11.5f)
                reflectiveQuadTo(460f, 680f)
                reflectiveQuadToRelative(-11.5f, -28.5f)
                reflectiveQuadTo(420f, 640f)
                reflectiveQuadToRelative(-28.5f, 11.5f)
                reflectiveQuadTo(380f, 680f)
                reflectiveQuadToRelative(11.5f, 28.5f)
                reflectiveQuadTo(420f, 720f)
                moveToRelative(140f, 0f)
                quadToRelative(17f, 0f, 28.5f, -11.5f)
                reflectiveQuadTo(600f, 680f)
                reflectiveQuadToRelative(-11.5f, -28.5f)
                reflectiveQuadTo(560f, 640f)
                reflectiveQuadToRelative(-28.5f, 11.5f)
                reflectiveQuadTo(520f, 680f)
                reflectiveQuadToRelative(11.5f, 28.5f)
                reflectiveQuadTo(560f, 720f)
                moveToRelative(10f, -390f)
                lineToRelative(-58f, -58f)
                quadToRelative(26f, -24f, 58f, -38f)
                reflectiveQuadToRelative(70f, -14f)
                reflectiveQuadToRelative(70f, 14f)
                reflectiveQuadToRelative(58f, 38f)
                lineToRelative(-58f, 58f)
                quadToRelative(-14f, -14f, -31.5f, -22f)
                reflectiveQuadToRelative(-38.5f, -8f)
                reflectiveQuadToRelative(-38.5f, 8f)
                reflectiveQuadToRelative(-31.5f, 22f)
                moveTo(470f, 230f)
                lineToRelative(-56f, -56f)
                quadToRelative(44f, -44f, 102f, -69f)
                reflectiveQuadToRelative(124f, -25f)
                reflectiveQuadToRelative(124f, 25f)
                reflectiveQuadToRelative(102f, 69f)
                lineToRelative(-56f, 56f)
                quadToRelative(-33f, -33f, -76.5f, -51.5f)
                reflectiveQuadTo(640f, 160f)
                reflectiveQuadToRelative(-93.5f, 18.5f)
                reflectiveQuadTo(470f, 230f)
                moveTo(200f, 760f)
                verticalLineToRelative(-160f)
                close()
            }
        }.build()

        return _Router!!
    }

@Suppress("ObjectPropertyName")
private var _Router: ImageVector? = null
