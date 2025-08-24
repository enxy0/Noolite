package com.enxy.noolite.presentation.ui.common

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.enxy.noolite.presentation.utils.ThemedPreview

@Composable
fun ShapeIcon(
    painter: Painter,
    padding: Dp = 12.dp,
    shape: Shape = RoundedCornerShape(16.dp),
    color: Color = MaterialTheme.colorScheme.surfaceContainer,
) {
    Icon(
        painter = painter,
        contentDescription = null,
        modifier = Modifier
            .defaultMinSize(56.dp, 56.dp)
            .background(color, shape)
            .padding(padding)
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
