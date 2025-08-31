package com.enxy.noolite.core.ui.compose

import android.content.res.Configuration
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import com.enxy.noolite.core.ui.FakeUiDataProvider

@Composable
fun AppTextField(
    text: String,
    label: String,
    modifier: Modifier = Modifier,
    focusManager: FocusManager = LocalFocusManager.current,
    onTextChange: (text: String) -> Unit,
) {
    OutlinedTextField(
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
            label = "Адрес шлюза",
            onTextChange = {}
        )
    }
}
