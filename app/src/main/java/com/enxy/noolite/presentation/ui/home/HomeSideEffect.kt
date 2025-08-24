package com.enxy.noolite.presentation.ui.home

sealed interface HomeSideEffect {
    data class Message(val message: String) : HomeSideEffect
}
