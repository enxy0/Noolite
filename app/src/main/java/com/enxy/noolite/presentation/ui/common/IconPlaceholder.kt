package com.enxy.noolite.presentation.ui.common

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.enxy.noolite.R
import com.enxy.noolite.presentation.ui.theme.AppTheme
import com.enxy.noolite.presentation.utils.ThemedPreview

@Composable
fun IconPlaceholder(
    painter: Painter,
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.normal),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Icon(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .size(56.dp)
                .background(AppTheme.colors.surface, AppTheme.shapes.medium)
                .padding(12.dp)
        )
        Text(text = text, style = AppTheme.typography.body2)
    }
}

@Composable
fun IconPlaceholder(
    imageVector: ImageVector,
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.normal),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = null,
            modifier = Modifier
                .size(56.dp)
                .background(AppTheme.colors.surface, AppTheme.shapes.medium)
                .padding(14.dp)
        )
        Text(text = text, style = AppTheme.typography.body2)
    }
}

@Preview("Placeholder")
@Preview("Placeholder (dark)", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewPlaceholder() {
    ThemedPreview {
        IconPlaceholder(
            imageVector = Icons.Outlined.FavoriteBorder,
            text = stringResource(R.string.home_favorite_empty)
        )
    }
}
