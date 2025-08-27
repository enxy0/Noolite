package com.enxy.noolite.features.detail

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.enxy.noolite.R
import com.enxy.noolite.domain.features.actions.model.ChannelAction
import com.enxy.noolite.domain.features.common.Channel
import com.enxy.noolite.features.common.IconActionButton
import com.enxy.noolite.utils.FakeUiDataProvider
import com.enxy.noolite.utils.ThemedPreview

private val ChannelShape = RoundedCornerShape(24.dp)

@Composable
fun Channel(
    channel: Channel,
    onChannelActionClick: (action: ChannelAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(ChannelShape)
            .background(MaterialTheme.colorScheme.surfaceContainer, ChannelShape)
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        if (channel.hasLight) {
            LightContent(
                channel = channel,
                onLightClick = onChannelActionClick,
            )
        }
        if (channel.hasBrightness) {
            BrightnessContent(
                channel = channel,
                onBrightnessChange = onChannelActionClick,
            )
        }
        if (channel.hasBacklight) {
            BacklightContent(
                channel = channel,
                onAction = onChannelActionClick,
            )
        }
    }
}

@Composable
private fun BacklightContent(
    channel: Channel,
    onAction: (action: ChannelAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Column(Modifier.weight(1f)) {
            Text(
                text = stringResource(R.string.channels_backlight),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            Text(
                text = stringResource(R.string.channels_backlight_description),
                style = MaterialTheme.typography.bodySmall
            )
        }
        IconActionButton(
            painter = painterResource(R.drawable.ic_start),
            onClick = { onAction(ChannelAction.StartOverflow(channel.id)) }
        )
        IconActionButton(
            painter = painterResource(R.drawable.ic_stop),
            onClick = { onAction(ChannelAction.StopOverflow(channel.id)) }
        )
        IconActionButton(
            painter = painterResource(R.drawable.ic_refresh),
            onClick = { onAction(ChannelAction.ChangeColor(channel.id)) }
        )
    }
}

@Composable
private fun BrightnessContent(
    channel: Channel,
    onBrightnessChange: (action: ChannelAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    var position by rememberSaveable { mutableFloatStateOf(0f) }
    Slider(
        value = position,
        onValueChange = { position = it },
        valueRange = 0f..100f,
        onValueChangeFinished = {
            onBrightnessChange(ChannelAction.ChangeBrightness(channel.id, position.toInt()))
        },
        modifier = modifier,
    )
}

@Composable
private fun LightContent(
    channel: Channel,
    onLightClick: (action: ChannelAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Column(Modifier.weight(1f)) {
            Text(
                text = channel.name,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = stringResource(R.string.channels_turn_on_off),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        IconActionButton(
            painter = painterResource(R.drawable.ic_on),
            onClick = { onLightClick(ChannelAction.Toggle(channel.id)) }
        )
    }
}

@Preview("Channel type 0")
@Preview("Channel type 0 (dark)", uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun PreviewChannel0() {
    ThemedPreview {
        Channel(
            channel = FakeUiDataProvider.getChannelType0(),
            onChannelActionClick = {},
        )
    }
}

@Preview("Channel type 1")
@Preview("Channel type 1 (dark)", uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun PreviewChannel1() {
    ThemedPreview {
        Channel(
            channel = FakeUiDataProvider.getChannelType1(),
            onChannelActionClick = {},
        )
    }
}

@Preview("Channel type 3")
@Preview("Channel type 3 (dark)", uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun PreviewChannel3() {
    ThemedPreview {
        Channel(
            channel = FakeUiDataProvider.getChannelType3(),
            onChannelActionClick = {},
        )
    }
}
