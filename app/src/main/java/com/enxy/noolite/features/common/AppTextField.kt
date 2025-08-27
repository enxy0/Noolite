package com.enxy.noolite.features.common

import android.content.res.Configuration
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import com.enxy.noolite.R
import com.enxy.noolite.utils.FakeUiDataProvider
import com.enxy.noolite.utils.ThemedPreview

@Composable
fun AppTextField(
    text: String,
    label: String,
    modifier: Modifier = Modifier,
    focusManager: FocusManager = LocalFocusManager.current,
    onTextChange: (text: String) -> Unit,
) {
    TextField(
        value = text,
        onValueChange = { value -> onTextChange(value) },
        label = { Text(label) },
        modifier = modifier,
        keyboardOptions = KeyboardOptions(
            autoCorrectEnabled = false,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
        singleLine = true
    )
}

@Preview("TextField")
@Preview("TextField (dark)", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewTextField() {
    ThemedPreview {
        AppTextField(
            text = FakeUiDataProvider.getSettings().apiUrl,
            label = stringResource(R.string.settings_server_title),
            onTextChange = {}
        )
    }
}
