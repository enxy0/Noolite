package com.enxy.noolite.feature.script

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.enxy.noolite.core.model.Channel
import com.enxy.noolite.core.model.ChannelAction
import com.enxy.noolite.core.model.GroupAction
import com.enxy.noolite.core.ui.FakeUiDataProvider
import com.enxy.noolite.core.ui.NooliteIcons
import com.enxy.noolite.core.ui.compose.ThemedPreview
import com.enxy.noolite.core.ui.compose.TopAppBar
import com.enxy.noolite.core.ui.extensions.firstOfTypeOrNull
import com.enxy.noolite.core.ui.icons.ArrowBack
import com.enxy.noolite.feature.script.model.ScriptChannel
import com.enxy.noolite.feature.script.model.ScriptGroup
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun ScriptContent(
    component: ScriptComponent,
    modifier: Modifier = Modifier,
) {
    val state by component.collectAsState()
    ScriptScaffold(
        state = state,
        onBackClick = component::onBackClick,
        onCreateScriptClick = component::onCreateScriptClick,
        onNameChange = component::onNameChange,
        onAddGroupAction = component::onAddGroupAction,
        onRemoveGroupAction = component::onRemoveGroupAction,
        onAddChannelAction = component::onAddChannelAction,
        onRemoveChannelAction = component::onRemoveChannelAction,
        onExpandGroupClick = component::onExpandGroupClick,
        onExpandChannelClick = component::onExpandChannelClick,
        modifier = modifier,
    )
}

@Composable
private fun ScriptScaffold(
    state: ScriptState,
    onBackClick: () -> Unit,
    onCreateScriptClick: () -> Unit,
    onNameChange: (name: String) -> Unit,
    onAddGroupAction: (action: GroupAction) -> Unit,
    onRemoveGroupAction: (action: GroupAction) -> Unit,
    onAddChannelAction: (action: ChannelAction) -> Unit,
    onRemoveChannelAction: (action: ChannelAction) -> Unit,
    onExpandGroupClick: (groupId: Int, expanded: Boolean) -> Unit,
    onExpandChannelClick: (channelId: Int, expanded: Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val hazeState = rememberHazeState()
    Scaffold(
        topBar = {
            ScriptsTopAppBar(
                hazeState = hazeState,
                onBackClick = onBackClick,
            )
        },
        floatingActionButton = {
            ScriptCreateButton(onClick = onCreateScriptClick)
        },
        floatingActionButtonPosition = FabPosition.Center,
        contentWindowInsets = WindowInsets.systemBars.add(WindowInsets.displayCutout),
        modifier = modifier,
    ) { contentPadding ->
        when (state) {
            is ScriptState.Content -> {
                ScriptContentState(
                    state = state,
                    contentPadding = contentPadding,
                    onNameChange = onNameChange,
                    onAddGroupAction = onAddGroupAction,
                    onRemoveGroupAction = onRemoveGroupAction,
                    onAddChannelAction = onAddChannelAction,
                    onRemoveChannelAction = onRemoveChannelAction,
                    onExpandGroupClick = onExpandGroupClick,
                    onExpandChannelClick = onExpandChannelClick,
                    modifier = Modifier
                        .fillMaxSize()
                        .imePadding()
                        .hazeSource(state = hazeState)
                )
            }
            is ScriptState.Loading -> Unit
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScriptsTopAppBar(
    hazeState: HazeState,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(stringResource(R.string.script_title)) },
        hazeState = hazeState,
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = NooliteIcons.ArrowBack,
                    contentDescription = null,
                )
            }
        },
        modifier = modifier,
    )
}

@Composable
private fun ScriptContentState(
    state: ScriptState.Content,
    contentPadding: PaddingValues,
    onNameChange: (String) -> Unit,
    onAddGroupAction: (GroupAction) -> Unit,
    onRemoveGroupAction: (GroupAction) -> Unit,
    onAddChannelAction: (ChannelAction) -> Unit,
    onRemoveChannelAction: (ChannelAction) -> Unit,
    onExpandGroupClick: (Int, Boolean) -> Unit,
    onExpandChannelClick: (Int, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        contentPadding = contentPadding,
        modifier = modifier
    ) {
        item(
            key = ScriptContentType.ScriptNameTitle.name,
            contentType = ScriptContentType.ScriptNameTitle,
        ) {
            SectionTitle(
                name = stringResource(R.string.script_name_title),
                modifier = Modifier
                    .padding(start = 16.dp, top = 16.dp, end = 16.dp)
            )
        }
        item(
            key = ScriptContentType.ScriptName.name,
            contentType = ScriptContentType.ScriptName,
        ) {
            ScriptName(
                name = state.name,
                onNameChange = onNameChange,
                error = state.error,
                modifier = Modifier
                    .padding(start = 16.dp, top = 16.dp, end = 16.dp)
            )
        }
        item(
            key = ScriptContentType.ScriptActionTitle.name,
            contentType = ScriptContentType.ScriptActionTitle,
        ) {
            SectionTitle(
                name = stringResource(R.string.script_action_title),
                modifier = Modifier
                    .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 12.dp)
            )
        }
        items(
            items = state.groups,
            contentType = { ScriptContentType.Group },
            key = { scriptGroup -> scriptGroup.group.id },
        ) { scriptGroup ->
            Group(
                scriptGroup = scriptGroup,
                onAddGroupAction = onAddGroupAction,
                onRemoveGroupAction = onRemoveGroupAction,
                onAddChannelAction = onAddChannelAction,
                onRemoveChannelAction = onRemoveChannelAction,
                onExpandGroupClick = onExpandGroupClick,
                onExpandChannelClick = onExpandChannelClick,
                modifier = Modifier
                    .padding(vertical = 4.dp, horizontal = 16.dp)
            )
        }
        item(
            key = ScriptContentType.Spacer.name,
            contentType = ScriptContentType.Spacer,
        ) {
            Spacer(Modifier.height(88.dp))
        }
    }
}

private enum class ScriptContentType {
    ScriptNameTitle,
    ScriptName,
    ScriptActionTitle,
    Group,
    Spacer,
}

@Composable
private fun SectionTitle(
    name: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = name,
        style = MaterialTheme.typography.headlineSmall,
        modifier = modifier,
    )
}

@Composable
private fun ScriptName(
    name: String,
    error: String,
    onNameChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current
    Column(modifier) {
        TextField(
            value = name,
            onValueChange = onNameChange,
            placeholder = { Text(text = stringResource(R.string.script_name_placeholder)) },
            keyboardOptions = KeyboardOptions(
                autoCorrectEnabled = false,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            singleLine = true,
            isError = error.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        )
        AnimatedContent(error) { error ->
            if (error.isNotBlank()) {
                Text(
                    text = error,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 4.dp, start = 12.dp, end = 12.dp)
                )
            }
        }
    }
}

@Composable
private fun Group(
    scriptGroup: ScriptGroup,
    onAddGroupAction: (action: GroupAction) -> Unit,
    onAddChannelAction: (action: ChannelAction) -> Unit,
    onRemoveGroupAction: (action: GroupAction) -> Unit,
    onRemoveChannelAction: (action: ChannelAction) -> Unit,
    onExpandGroupClick: (groupId: Int, expanded: Boolean) -> Unit,
    onExpandChannelClick: (channelId: Int, expanded: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .clickable {
                focusManager.clearFocus()
                onExpandGroupClick(scriptGroup.group.id, !scriptGroup.expanded)
            }
            .padding(16.dp)
    ) {
        Text(
            text = scriptGroup.group.name,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = scriptGroup.group.channelNames,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        AnimatedVisibility(scriptGroup.expanded) {
            Column {
                Spacer(Modifier.height(16.dp))
                GroupLightContent(
                    scriptGroup = scriptGroup,
                    onAddGroupAction = onAddGroupAction,
                    onRemoveGroupAction = onRemoveGroupAction,
                )
                for (scriptChannel in scriptGroup.channels) {
                    Spacer(Modifier.height(12.dp))
                    Channel(
                        channel = scriptChannel.channel,
                        actions = scriptChannel.actions,
                        expanded = scriptChannel.expanded,
                        onAddChannelAction = onAddChannelAction,
                        onRemoveChannelAction = onRemoveChannelAction,
                        onChannelClick = {
                            onExpandChannelClick(
                                scriptChannel.channel.id,
                                !scriptChannel.expanded,
                            )
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun GroupLightContent(
    scriptGroup: ScriptGroup,
    onAddGroupAction: (action: GroupAction) -> Unit,
    onRemoveGroupAction: (action: GroupAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        val turnOnAction: GroupAction.TurnOn? = scriptGroup.actions.firstOfTypeOrNull()
        CheckableAction(
            text = stringResource(R.string.script_group_turn_on),
            isChecked = turnOnAction != null,
            onActionClick = { isChecked ->
                if (isChecked) {
                    onAddGroupAction(GroupAction.TurnOn(scriptGroup.group))
                } else {
                    onRemoveGroupAction(turnOnAction!!)
                }
            }
        )
        val turnOffAction: GroupAction.TurnOff? = scriptGroup.actions.firstOfTypeOrNull()
        CheckableAction(
            text = stringResource(R.string.script_group_turn_off),
            isChecked = turnOffAction != null,
            onActionClick = { isChecked ->
                if (isChecked) {
                    onAddGroupAction(GroupAction.TurnOff(scriptGroup.group))
                } else {
                    onRemoveGroupAction(turnOffAction!!)
                }
            }
        )
    }
}

private val ChannelShape = RoundedCornerShape(12.dp)

@Composable
private fun Channel(
    channel: Channel,
    actions: Set<ChannelAction>,
    expanded: Boolean,
    onAddChannelAction: (action: ChannelAction) -> Unit,
    onRemoveChannelAction: (action: ChannelAction) -> Unit,
    onChannelClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(ChannelShape)
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, ChannelShape)
            .clickable(onClick = onChannelClick)
            .padding(16.dp)
    ) {
        Text(channel.name)
        AnimatedVisibility(expanded) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(top = 16.dp),
            ) {
                if (channel.hasLight) {
                    ChannelLightContent(
                        channel = channel,
                        actions = actions,
                        onAddChannelAction = onAddChannelAction,
                        onRemoveChannelAction = onRemoveChannelAction,
                    )
                }
                if (channel.hasBrightness) {
                    ChannelBrightnessContent(
                        channel = channel,
                        actions = actions,
                        onAddChannelAction = onAddChannelAction,
                        onRemoveChannelAction = onRemoveChannelAction,
                    )
                }
                if (channel.hasBacklight) {
                    ChannelBacklightContent(
                        channel = channel,
                        actions = actions,
                        onAddChannelAction = onAddChannelAction,
                        onRemoveChannelAction = onRemoveChannelAction,
                    )
                }
            }
        }
    }
}

@Composable
private fun ChannelLightContent(
    channel: Channel,
    actions: Set<ChannelAction>,
    onAddChannelAction: (action: ChannelAction) -> Unit,
    onRemoveChannelAction: (action: ChannelAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        val turnOnAction: ChannelAction.TurnOn? = actions.firstOfTypeOrNull()
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
        val turnOffAction: ChannelAction.TurnOff? = actions.firstOfTypeOrNull()
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
}

@Composable
private fun ChannelBrightnessContent(
    channel: Channel,
    actions: Set<ChannelAction>,
    onAddChannelAction: (action: ChannelAction) -> Unit,
    onRemoveChannelAction: (action: ChannelAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val changeBrightnessAction: ChannelAction.ChangeBrightness? = actions.firstOfTypeOrNull()
    SliderAction(
        text = stringResource(R.string.script_channel_light_brightness),
        isChecked = changeBrightnessAction != null,
        initialValue = changeBrightnessAction?.brightness ?: 0,
        onActionChange = { isChecked, brightness ->
            if (isChecked) {
                onAddChannelAction(ChannelAction.ChangeBrightness(channel.id, brightness))
            } else {
                onRemoveChannelAction(changeBrightnessAction!!)
            }
        },
        modifier = modifier,
    )
}

@Composable
private fun ChannelBacklightContent(
    channel: Channel,
    actions: Set<ChannelAction>,
    onAddChannelAction: (action: ChannelAction) -> Unit,
    onRemoveChannelAction: (action: ChannelAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        val changeColorAction: ChannelAction.ChangeColor? = actions.firstOfTypeOrNull()
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
        val startOverflowAction: ChannelAction.StartOverflow? = actions.firstOfTypeOrNull()
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
        val stopOverflowAction: ChannelAction.StopOverflow? = actions.firstOfTypeOrNull()
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
            .clip(ChannelShape)
            .background(colorAction(isChecked))
            .clickable { onActionClick(!isChecked) }
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = { value -> onActionClick(value) }
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun SliderAction(
    text: String,
    isChecked: Boolean,
    onActionChange: (isChecked: Boolean, value: Int) -> Unit,
    modifier: Modifier = Modifier,
    initialValue: Int = 0,
) {
    var value by remember { mutableFloatStateOf(initialValue.toFloat()) }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .background(colorAction(isChecked))
            .clickable { onActionChange(!isChecked, value.toInt()) }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = { change -> onActionChange(change, value.toInt()) }
            )
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f),
            )
            Text(
                text = "${value.toInt()}%",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(end = 16.dp)
            )
        }
        Slider(
            value = value,
            onValueChange = { value = it },
            enabled = isChecked,
            valueRange = 0f..100f,
            colors = if (isChecked) {
                SliderDefaults.colors(
                    inactiveTrackColor = MaterialTheme.colorScheme.secondary.copy(alpha = .5f)
                )
            } else {
                SliderDefaults.colors()
            },
            onValueChangeFinished = { onActionChange(true, value.toInt()) },
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Composable
@ReadOnlyComposable
private fun colorAction(checked: Boolean) = if (checked) {
    MaterialTheme.colorScheme.secondaryContainer
} else {
    MaterialTheme.colorScheme.surfaceContainerLow
}

@Composable
private fun ScriptCreateButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .heightIn(56.dp)
    ) {

        Text(stringResource(R.string.script_create))
    }
}

@Preview("Content Home Screen")
@Preview("Content Home Screen (dark)", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewScript() {
    val scriptGroups = FakeUiDataProvider.getGroups().map { group ->
        ScriptGroup(
            group = group,
            expanded = true,
            channels = group.channels.map { channel ->
                ScriptChannel(channel)
            }
        )
    }
    ThemedPreview {
        ScriptScaffold(
            state = ScriptState.Content(name = "", error = "", groups = scriptGroups),
            onBackClick = {},
            onCreateScriptClick = {},
            onNameChange = {},
            onAddGroupAction = {},
            onRemoveGroupAction = {},
            onAddChannelAction = {},
            onRemoveChannelAction = {},
            onExpandGroupClick = { _, _ -> },
            onExpandChannelClick = { _, _ -> },
        )
    }
}
