package com.enxy.noolite.feature.script

import androidx.compose.runtime.Immutable
import com.enxy.noolite.feature.script.model.ScriptGroup
import kotlinx.serialization.Serializable

@Immutable
@Serializable
sealed interface ScriptState {
    @Immutable
    @Serializable
    data object Loading : ScriptState

    @Immutable
    @Serializable
    data class Content(
        val name: String,
        val error: String,
        val groups: List<ScriptGroup>,
    ) : ScriptState
}
