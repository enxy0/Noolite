package com.enxy.noolite.core.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.enxy.noolite.core.ui.NooliteIcons

val NooliteIcons.Server: ImageVector
    get() {
        if (_Server != null) return _Server!!

        _Server = ImageVector.Builder(
            name = "Server",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                stroke = SolidColor(Color(0xFF0F172A)),
                strokeLineWidth = 1.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(21.75f, 17.25f)
                verticalLineTo(17.0223f)
                curveTo(21.75f, 16.6753f, 21.7099f, 16.3294f, 21.6304f, 15.9916f)
                lineTo(19.3622f, 6.35199f)
                curveTo(19.0035f, 4.82745f, 17.6431f, 3.75f, 16.077f, 3.75f)
                horizontalLineTo(7.92305f)
                curveTo(6.35688f, 3.75f, 4.99648f, 4.82745f, 4.63777f, 6.35199f)
                lineTo(2.36962f, 15.9916f)
                curveTo(2.29014f, 16.3294f, 2.25f, 16.6753f, 2.25f, 17.0223f)
                verticalLineTo(17.25f)
                moveTo(21.75f, 17.25f)
                curveTo(21.75f, 18.9069f, 20.4069f, 20.25f, 18.75f, 20.25f)
                horizontalLineTo(5.25f)
                curveTo(3.59315f, 20.25f, 2.25f, 18.9069f, 2.25f, 17.25f)
                moveTo(21.75f, 17.25f)
                curveTo(21.75f, 15.5931f, 20.4069f, 14.25f, 18.75f, 14.25f)
                horizontalLineTo(5.25f)
                curveTo(3.59315f, 14.25f, 2.25f, 15.5931f, 2.25f, 17.25f)
                moveTo(18.75f, 17.25f)
                horizontalLineTo(18.7575f)
                verticalLineTo(17.2575f)
                horizontalLineTo(18.75f)
                verticalLineTo(17.25f)
                close()
                moveTo(15.75f, 17.25f)
                horizontalLineTo(15.7575f)
                verticalLineTo(17.2575f)
                horizontalLineTo(15.75f)
                verticalLineTo(17.25f)
                close()
            }
        }.build()

        return _Server!!
    }

private var _Server: ImageVector? = null

