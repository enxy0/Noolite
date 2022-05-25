package com.enxy.noolite.presentation.ui.common

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.enxy.noolite.R
import com.enxy.noolite.presentation.ui.theme.AppTheme
import com.enxy.noolite.presentation.utils.ThemedPreview

@Composable
fun ActionButton(
    painter: Painter,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current),
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(AppTheme.shapes.medium)
            .background(color = AppTheme.colors.surfaceVariant)
            .then(modifier)
            .clickable(onClick = onClick)
            .padding(AppTheme.dimensions.normal)
    ) {
        Icon(
            painter = painter,
            contentDescription = null,
            tint = tint
        )
    }
}

@Preview("Action")
@Preview("Action (dark)", uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun PreviewActionButton() {
    ThemedPreview {
        ActionButton(
            painter = painterResource(R.drawable.ic_on),
            onClick = {}
        )
    }
}
