package com.enxy.noolite.feature.home

sealed interface HomeSideEffect {
    data class Message(val message: String) : HomeSideEffect
}
