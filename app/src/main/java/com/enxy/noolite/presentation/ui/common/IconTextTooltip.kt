package com.enxy.noolite.presentation.ui.common

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.enxy.noolite.R
import com.enxy.noolite.ui.theme.NooliteTheme

@Composable
fun IconTextTooltip(
    painter: Painter,
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        ShapeIcon(painter)
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Preview("Placeholder")
@Preview("Placeholder (dark)", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewPlaceholder() {
    NooliteTheme {
        IconTextTooltip(
            painter = rememberVectorPainter(Icons.Outlined.FavoriteBorder),
            text = stringResource(R.string.home_favorite_empty)
        )
    }
}
