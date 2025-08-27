package com.enxy.noolite.features.script

import com.enxy.noolite.features.script.model.ScriptGroup
import kotlinx.serialization.Serializable

@Serializable
sealed interface ScriptState {
    @Serializable
    data object Loading : ScriptState

    @Serializable
    data class Content(
        val name: String,
        val error: String,
        val groups: List<ScriptGroup>,
    ) : ScriptState
}
