package com.enxy.noolite.presentation.ui.script

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.Scaffold
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.enxy.domain.features.actions.model.ChannelAction
import com.enxy.domain.features.actions.model.GroupAction
import com.enxy.domain.features.common.Channel
import com.enxy.noolite.R
import com.enxy.noolite.presentation.ui.common.AppBar
import com.enxy.noolite.presentation.ui.script.model.ScriptChannel
import com.enxy.noolite.presentation.ui.script.model.ScriptGroup
import com.enxy.noolite.presentation.ui.theme.AppTheme
import com.enxy.noolite.presentation.utils.FakeUiDataProvider
import com.enxy.noolite.presentation.utils.ThemedPreview
import com.enxy.noolite.presentation.utils.extensions.firstOfTypeOrNull
import org.koin.androidx.compose.getViewModel

private data class ScriptState(
    val onBackClick: () -> Unit = {},
    val onCreateScriptClick: () -> Unit = {},
    val onNameChange: (name: String) -> Unit = {},
    val onAddGroupAction: (action: GroupAction) -> Unit = {},
    val onRemoveGroupAction: (action: GroupAction) -> Unit = {},
    val onAddChannelAction: (action: ChannelAction) -> Unit = {},
    val onRemoveChannelAction: (action: ChannelAction) -> Unit = {}
)

private val DP_ACTION_SPACING = 4.dp

@Composable
fun ScriptScreen(
    onBackClick: () -> Unit,
    viewModel: ScriptViewModel = getViewModel()
) {
    val name by viewModel.name
    val isError by viewModel.isError
    val groups = viewModel.groups
    val state = rememberScriptState(onBackClick, viewModel)
    ScriptScaffold(
        state = state,
        name = name,
        groups = groups,
        isError = isError
    )
}

@Composable
private fun rememberScriptState(
    onBackClick: () -> Unit,
    viewModel: ScriptViewModel
) = remember {
    ScriptState(
        onBackClick = onBackClick,
        onNameChange = viewModel::onNameChange,
        onCreateScriptClick = viewModel::onCreateScriptClick,
        onAddGroupAction = viewModel::onAddGroupAction,
        onRemoveGroupAction = viewModel::onRemoveGroupAction,
        onAddChannelAction = viewModel::onAddChannelAction,
        onRemoveChannelAction = viewModel::onRemoveChannelAction,
    )
}

@Composable
private fun ScriptScaffold(
    state: ScriptState,
    name: String,
    isError: Boolean,
    groups: List<ScriptGroup>
) {
    Scaffold(topBar = { AppBar(stringResource(R.string.script_title), state.onBackClick) }) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            ScriptContent(state, name, isError, groups)
            ScriptCreateButton(state.onCreateScriptClick)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ScriptContent(
    state: ScriptState,
    name: String,
    isError: Boolean,
    scriptGroups: List<ScriptGroup>
) {
    LazyColumn(
        contentPadding = PaddingValues(
            start = AppTheme.dimensions.normal,
            top = AppTheme.dimensions.normal,
            end = AppTheme.dimensions.normal,
            bottom = 80.dp
        )
    ) {
        item { SectionTitle(stringResource(R.string.script_name_title)) }
        item { Spacer(Modifier.height(AppTheme.dimensions.normal)) }
        item { SectionScriptName(name, state, isError) }
        item { Spacer(Modifier.height(AppTheme.dimensions.bigger)) }
        item { SectionTitle(stringResource(R.string.script_action_title)) }
        item { Spacer(Modifier.height(AppTheme.dimensions.smallest)) }
        items(
            items = scriptGroups,
            key = { scriptGroup -> scriptGroup.group.id }
        ) { scriptGroup ->
            Group(
                state = state,
                scriptGroup = scriptGroup,
                modifier = Modifier
                    .animateItemPlacement()
                    .padding(vertical = AppTheme.dimensions.smallest)
            )
        }
    }
}

@Composable
private fun SectionTitle(name: String) {
    Text(text = name, style = AppTheme.typography.h6)
}

@Composable
private fun SectionScriptName(
    name: String,
    state: ScriptState,
    isError: Boolean
) {
    val focusManager = LocalFocusManager.current
    Column {
        TextField(
            value = name,
            onValueChange = { value -> state.onNameChange(value) },
            placeholder = { Text(text = stringResource(R.string.script_name_placeholder)) },
            keyboardOptions = KeyboardOptions(autoCorrect = false, imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            singleLine = true,
            isError = isError,
            modifier = Modifier.fillMaxWidth()
        )
        if (isError) {
            Text(
                text = stringResource(id = R.string.script_name_error),
                style = AppTheme.typography.caption,
                color = AppTheme.colors.error,
                modifier = Modifier.padding(horizontal = AppTheme.dimensions.normal)
            )
        }
    }
}

@Composable
private fun Group(
    state: ScriptState,
    scriptGroup: ScriptGroup,
    modifier: Modifier = Modifier
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val group = scriptGroup.group
    Column(
        modifier = modifier then Modifier
            .fillMaxWidth()
            .clip(AppTheme.shapes.medium)
            .background(AppTheme.colors.surface)
            .clickable { expanded = !expanded }
            .padding(AppTheme.dimensions.normal)
    ) {
        Text(text = group.name)
        Text(
            text = group.channelNames,
            style = AppTheme.typography.body2,
            maxLines = 1
        )
        if (expanded) {
            Column {
                GroupLightContent(state, scriptGroup)
                for (channel in scriptGroup.channels) {
                    Spacer(Modifier.height(AppTheme.dimensions.smallest))
                    Channel(state, channel)
                }
            }
        }
    }
}

@Composable
private fun GroupLightContent(
    state: ScriptState,
    scriptGroup: ScriptGroup
) {
    val turnOnAction: GroupAction.TurnOn? = scriptGroup.actions.firstOfTypeOrNull()
    val turnOffAction: GroupAction.TurnOff? = scriptGroup.actions.firstOfTypeOrNull()
    Spacer(Modifier.height(AppTheme.dimensions.normal))
    CheckableAction(
        text = stringResource(R.string.script_group_turn_on),
        isChecked = turnOnAction != null,
        onActionClick = { isChecked ->
            if (isChecked) {
                state.onAddGroupAction(GroupAction.TurnOn(scriptGroup.group))
            } else {
                state.onRemoveGroupAction(turnOnAction!!)
            }
        }
    )
    Spacer(Modifier.height(DP_ACTION_SPACING))
    CheckableAction(
        text = stringResource(R.string.script_group_turn_off),
        isChecked = turnOffAction != null,
        onActionClick = { isChecked ->
            if (isChecked) {
                state.onAddGroupAction(GroupAction.TurnOff(scriptGroup.group))
            } else {
                state.onRemoveGroupAction(turnOffAction!!)
            }
        }
    )
}

@Composable
private fun Channel(state: ScriptState, scriptChannel: ScriptChannel) {
    var expanded by remember { mutableStateOf(false) }
    val channel = scriptChannel.channel
    val actions = scriptChannel.actions
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(AppTheme.shapes.small)
            .border(1.dp, AppTheme.colors.surfaceVariant, AppTheme.shapes.small)
            .clickable { expanded = !expanded }
            .padding(AppTheme.dimensions.normal)
    ) {
        Text(channel.name)
        AnimatedVisibility(expanded) {
            Column {
                if (channel.hasLight) {
                    ChannelLightContent(
                        channel = channel,
                        actions = actions,
                        onAddChannelAction = state.onAddChannelAction,
                        onRemoveChannelAction = state.onRemoveChannelAction
                    )
                }
                if (channel.hasBrightness) {
                    ChannelBrightnessContent(
                        channel = channel,
                        actions = actions,
                        onAddChannelAction = state.onAddChannelAction,
                        onRemoveChannelAction = state.onRemoveChannelAction
                    )
                }
                if (scriptChannel.channel.hasBacklight) {
                    ChannelBacklightContent(
                        channel = channel,
                        actions = actions,
                        onAddChannelAction = state.onAddChannelAction,
                        onRemoveChannelAction = state.onRemoveChannelAction
                    )
                }
            }
        }
    }
}

@Composable
private fun ChannelLightContent(
    channel: Channel,
    actions: List<ChannelAction>,
    onAddChannelAction: (action: ChannelAction) -> Unit,
    onRemoveChannelAction: (action: ChannelAction) -> Unit
) {
    val turnOnAction: ChannelAction.TurnOn? = actions.firstOfTypeOrNull()
    val turnOffAction: ChannelAction.TurnOff? = actions.firstOfTypeOrNull()
    Spacer(Modifier.height(AppTheme.dimensions.normal))
    CheckableAction(
        text = stringResource(R.string.script_channel_light_on),
        isChecked = turnOnAction != null,
        onActionClick = { isChecked ->
            if (isChecked) {
                onAddChannelAction(ChannelAction.TurnOn(channel.id))
            } else {
                onRemoveChannelAction(turnOnAction!!)
            }
        }
    )
    Spacer(Modifier.height(DP_ACTION_SPACING))
    CheckableAction(
        text = stringResource(R.string.script_channel_light_off),
        isChecked = turnOffAction != null,
        onActionClick = { isChecked ->
            if (isChecked) {
                onAddChannelAction(ChannelAction.TurnOff(channel.id))
            } else {
                onRemoveChannelAction(turnOffAction!!)
            }
        }
    )
}

@Composable
private fun ChannelBrightnessContent(
    channel: Channel,
    actions: List<ChannelAction>,
    onAddChannelAction: (action: ChannelAction) -> Unit,
    onRemoveChannelAction: (action: ChannelAction) -> Unit
) {
    val changeBrightnessAction: ChannelAction.ChangeBrightness? = actions.firstOfTypeOrNull()
    Spacer(Modifier.height(DP_ACTION_SPACING))
    SliderAction(
        text = stringResource(R.string.script_channel_light_brightness),
        isChecked = changeBrightnessAction != null,
        initialValue = changeBrightnessAction?.brightness ?: 0,
        onActionChange = { isChecked, brightness ->
            changeBrightnessAction?.let { action ->
                onRemoveChannelAction(action)
            }
            if (isChecked) {
                onAddChannelAction(ChannelAction.ChangeBrightness(channel.id, brightness))
            }
        }
    )
}

@Composable
private fun ChannelBacklightContent(
    channel: Channel,
    actions: List<ChannelAction>,
    onAddChannelAction: (action: ChannelAction) -> Unit,
    onRemoveChannelAction: (action: ChannelAction) -> Unit
) {
    val changeColorAction: ChannelAction.ChangeColor? = actions.firstOfTypeOrNull()
    val startOverflowAction: ChannelAction.StartOverflow? = actions.firstOfTypeOrNull()
    val stopOverflowAction: ChannelAction.StopOverflow? = actions.firstOfTypeOrNull()
    Spacer(Modifier.height(DP_ACTION_SPACING))
    CheckableAction(
        text = stringResource(R.string.script_channel_light_change_color),
        isChecked = changeColorAction != null,
        onActionClick = { isChecked ->
            if (isChecked) {
                onAddChannelAction(ChannelAction.ChangeColor(channel.id))
            } else {
                onRemoveChannelAction(changeColorAction!!)
            }
        }
    )
    Spacer(Modifier.height(DP_ACTION_SPACING))
    CheckableAction(
        text = stringResource(R.string.script_channel_light_start_overflow),
        isChecked = startOverflowAction != null,
        onActionClick = { isChecked ->
            if (isChecked) {
                onAddChannelAction(ChannelAction.StartOverflow(channel.id))
            } else {
                onRemoveChannelAction(startOverflowAction!!)
            }
        }
    )
    Spacer(Modifier.height(DP_ACTION_SPACING))
    CheckableAction(
        text = stringResource(R.string.script_channel_light_stop_overflow),
        isChecked = stopOverflowAction != null,
        onActionClick = { isChecked ->
            if (isChecked) {
                onAddChannelAction(ChannelAction.StopOverflow(channel.id))
            } else {
                onRemoveChannelAction(stopOverflowAction!!)
            }
        }
    )
}

@Composable
private fun CheckableAction(
    text: String,
    isChecked: Boolean,
    onActionClick: (isChecked: Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(AppTheme.shapes.small)
            .background(AppTheme.colors.surfaceVariant)
            .clickable { onActionClick(!isChecked) }
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = { value -> onActionClick(value) }
        )
        Text(
            text = text,
            style = AppTheme.typography.body2
        )
    }
}

@Composable
private fun SliderAction(
    text: String,
    isChecked: Boolean,
    onActionChange: (isChecked: Boolean, value: Int) -> Unit,
    initialValue: Int = 0
) {
    var value by remember { mutableStateOf(initialValue.toFloat()) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(AppTheme.shapes.small)
            .background(AppTheme.colors.surfaceVariant)
            .clickable { onActionChange(!isChecked, value.toInt()) }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = { change -> onActionChange(change, value.toInt()) }
            )
            Text(
                text = text,
                style = AppTheme.typography.body2
            )
        }
        Slider(
            value = value,
            onValueChange = { value = it },
            enabled = isChecked,
            valueRange = 0f..100f,
            onValueChangeFinished = { onActionChange(true, value.toInt()) },
            modifier = Modifier.padding(horizontal = AppTheme.dimensions.normal)
        )
    }
}

@Composable
private fun BoxScope.ScriptCreateButton(onCreateScriptClick: () -> Unit) {
    Button(
        onClick = onCreateScriptClick,
        shape = AppTheme.shapes.medium,
        elevation = null,
        modifier = Modifier
            .padding(AppTheme.dimensions.normal)
            .fillMaxWidth()
            .height(56.dp)
            .align(Alignment.BottomCenter)
    ) {
        Text(
            text = stringResource(R.string.script_create),
            style = AppTheme.typography.button1
        )
    }
}

@Preview("Content Home Screen")
@Preview("Content Home Screen (dark)", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewScript() {
    ThemedPreview {
        ScriptScaffold(
            state = ScriptState(),
            name = "",
            isError = true,
            groups = FakeUiDataProvider.getScriptGroups(),
        )
    }
}
