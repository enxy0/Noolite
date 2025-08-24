package com.enxy.noolite.presentation.ui.common

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
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
import com.enxy.noolite.ui.theme.NooliteTheme

@Composable
fun IconActionButton(
    painter: Painter,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
    containerColor: Color = MaterialTheme.colorScheme.secondaryContainer,
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(color = containerColor)
            .then(modifier)
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Icon(
            painter = painter,
            contentDescription = null,
            tint = contentColor,
        )
    }
}

@Preview("Action", showBackground = true)
@Preview("Action (dark)", uiMode = UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun PreviewActionButton() {
    NooliteTheme {
        IconActionButton(
            painter = painterResource(R.drawable.ic_on),
            onClick = {}
        )
    }
}
