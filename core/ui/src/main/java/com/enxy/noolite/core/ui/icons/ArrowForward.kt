package com.enxy.noolite.core.ui.icons

import androidx.compose.ui.graphics.vector.ImageVector
import com.enxy.noolite.core.ui.NooliteIcons

val NooliteIcons.ArrowForward: ImageVector
    get() {
        if (_arrowForward != null) {
            return _arrowForward!!
        }
        _arrowForward = nooliteIcon(name = "Rounded.ArrowForward", autoMirror = true) {
            noolitePath {
                moveTo(5.0f, 13.0f)
                horizontalLineToRelative(11.17f)
                lineToRelative(-4.88f, 4.88f)
                curveToRelative(-0.39f, 0.39f, -0.39f, 1.03f, 0.0f, 1.42f)
                curveToRelative(0.39f, 0.39f, 1.02f, 0.39f, 1.41f, 0.0f)
                lineToRelative(6.59f, -6.59f)
                curveToRelative(0.39f, -0.39f, 0.39f, -1.02f, 0.0f, -1.41f)
                lineToRelative(-6.58f, -6.6f)
                curveToRelative(-0.39f, -0.39f, -1.02f, -0.39f, -1.41f, 0.0f)
                curveToRelative(-0.39f, 0.39f, -0.39f, 1.02f, 0.0f, 1.41f)
                lineTo(16.17f, 11.0f)
                horizontalLineTo(5.0f)
                curveToRelative(-0.55f, 0.0f, -1.0f, 0.45f, -1.0f, 1.0f)
                reflectiveCurveToRelative(0.45f, 1.0f, 1.0f, 1.0f)
                close()
            }
        }
        return _arrowForward!!
    }

private var _arrowForward: ImageVector? = null
