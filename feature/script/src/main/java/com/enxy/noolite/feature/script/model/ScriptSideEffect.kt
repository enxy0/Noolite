package com.enxy.noolite.feature.script.model

sealed interface ScriptSideEffect {
    data class ScrollTo(val type: ScrollBlockType) : ScriptSideEffect
}

enum class ScrollBlockType {
    ScriptNameTextField,
}
