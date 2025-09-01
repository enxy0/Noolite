package com.enxy.noolite.core.ui.icons

import androidx.compose.ui.graphics.vector.ImageVector
import com.enxy.noolite.core.ui.NooliteIcons

val NooliteIcons.KeyboardArrowRight: ImageVector
    get() {
        if (_keyboardArrowRight != null) {
            return _keyboardArrowRight!!
        }
        _keyboardArrowRight = nooliteIcon(name = "Rounded.KeyboardArrowRight", autoMirror = true) {
            noolitePath {
                moveTo(9.29f, 15.88f)
                lineTo(13.17f, 12.0f)
                lineTo(9.29f, 8.12f)
                curveToRelative(-0.39f, -0.39f, -0.39f, -1.02f, 0.0f, -1.41f)
                curveToRelative(0.39f, -0.39f, 1.02f, -0.39f, 1.41f, 0.0f)
                lineToRelative(4.59f, 4.59f)
                curveToRelative(0.39f, 0.39f, 0.39f, 1.02f, 0.0f, 1.41f)
                lineTo(10.7f, 17.3f)
                curveToRelative(-0.39f, 0.39f, -1.02f, 0.39f, -1.41f, 0.0f)
                curveToRelative(-0.38f, -0.39f, -0.39f, -1.03f, 0.0f, -1.42f)
                close()
            }
        }
        return _keyboardArrowRight!!
    }

private var _keyboardArrowRight: ImageVector? = null
