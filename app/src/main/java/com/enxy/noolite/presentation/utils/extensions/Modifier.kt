package com.enxy.noolite.presentation.utils.extensions

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

fun Modifier.bottomDivider(color: Color): Modifier {
    return this.drawBehind {
        val strokeWidth = 1.dp.value * density
        val y = size.height - strokeWidth / 2

        drawLine(
            color,
            Offset(0f, y),
            Offset(size.width, y),
            strokeWidth
        )
    }
}
