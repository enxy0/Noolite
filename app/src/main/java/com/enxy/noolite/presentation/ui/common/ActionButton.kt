package com.enxy.noolite.presentation.ui.common

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.enxy.noolite.R
import com.enxy.noolite.presentation.utils.ThemedPreview

@Composable
fun ActionButton(
    painter: Painter,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current, // TODO: LocalContentAlpha.current is not available
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(color = MaterialTheme.colorScheme.surfaceVariant)
            .then(modifier)
            .clickable(onClick = onClick)
            .padding(16.dp)
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
