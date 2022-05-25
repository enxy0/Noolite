package com.enxy.noolite.presentation.ui.detail

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.enxy.domain.features.actions.model.ChannelAction
import com.enxy.domain.features.common.Channel
import com.enxy.noolite.R
import com.enxy.noolite.presentation.ui.common.ActionButton
import com.enxy.noolite.presentation.ui.theme.AppTheme
import com.enxy.noolite.presentation.utils.FakeUiDataProvider
import com.enxy.noolite.presentation.utils.ThemedPreview

data class ChannelState(
    val onAction: (action: ChannelAction) -> Unit = {}
)

@Composable
fun Channel(
    channel: Channel,
    state: ChannelState,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(AppTheme.shapes.large)
            .background(AppTheme.colors.surface)
            .fillMaxWidth()
            .padding(AppTheme.dimensions.normal)
    ) {
        Column {
            if (channel.hasLight) {
                LightContent(channel = channel, onLightClick = state.onAction)
            }
            if (channel.hasBrightness) {
                BrightnessContent(channel = channel, onBrightnessChange = state.onAction)
            }
            if (channel.hasBacklight) {
                BacklightContent(channel = channel, onAction = state.onAction)
            }
        }
    }
}

@Composable
private fun BacklightContent(
    channel: Channel,
    onAction: (action: ChannelAction) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.smallest)
    ) {
        Column(Modifier.weight(1f)) {
            Text(
                text = stringResource(R.string.channels_backlight),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            Text(
                text = stringResource(R.string.channels_backlight_description),
                style = AppTheme.typography.body2
            )
        }
        ActionButton(
            painter = painterResource(R.drawable.ic_start),
            onClick = { onAction(ChannelAction.StartOverflow(channel.id)) }
        )
        ActionButton(
            painter = painterResource(R.drawable.ic_stop),
            onClick = { onAction(ChannelAction.StopOverflow(channel.id)) }
        )
        ActionButton(
            painter = painterResource(R.drawable.ic_refresh),
            onClick = { onAction(ChannelAction.ChangeColor(channel.id)) }
        )
    }
}

@Composable
private fun BrightnessContent(
    channel: Channel,
    onBrightnessChange: (action: ChannelAction) -> Unit
) {
    var position by rememberSaveable { mutableStateOf(0f) }
    Slider(
        value = position,
        onValueChange = { position = it },
        valueRange = 0f..100f,
        onValueChangeFinished = {
            onBrightnessChange(ChannelAction.ChangeBrightness(channel.id, position.toInt()))
        }
    )
}

@Composable
private fun LightContent(
    channel: Channel,
    onLightClick: (action: ChannelAction) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.normal)
    ) {
        Column(Modifier.weight(1f)) {
            Text(
                text = channel.name,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            Text(
                text = stringResource(R.string.channels_turn_on_off),
                style = AppTheme.typography.body2
            )
        }
        ActionButton(
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
        Channel(FakeUiDataProvider.getChannelType0(), ChannelState())
    }
}

@Preview("Channel type 1")
@Preview("Channel type 1 (dark)", uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun PreviewChannel1() {
    ThemedPreview {
        Channel(FakeUiDataProvider.getChannelType1(), ChannelState())
    }
}

@Preview("Channel type 3")
@Preview("Channel type 3 (dark)", uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun PreviewChannel3() {
    ThemedPreview {
        Channel(FakeUiDataProvider.getChannelType3(), ChannelState())
    }
}
