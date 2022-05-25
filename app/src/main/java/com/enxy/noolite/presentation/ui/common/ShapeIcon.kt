package com.enxy.noolite.presentation.ui.common

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.enxy.noolite.R
import com.enxy.noolite.presentation.ui.theme.AppTheme
import com.enxy.noolite.presentation.utils.ThemedPreview

@Composable
fun ShapeIcon(
    painter: Painter,
    size: Dp = 56.dp,
    innerPadding: Dp = 14.dp,
    shape: Shape = AppTheme.shapes.medium,
    color: Color = AppTheme.colors.surface
) {
    Icon(
        painter = painter,
        contentDescription = null,
        modifier = Modifier
            .size(size)
            .background(color, shape)
            .padding(innerPadding)
    )
}

@Preview("App Icon")
@Preview("App Icon (dark)", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewAppIcon() {
    ThemedPreview {
        ShapeIcon(painterResource(R.drawable.ic_server))
    }
}
