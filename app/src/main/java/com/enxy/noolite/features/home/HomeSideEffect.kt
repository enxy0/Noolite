package com.enxy.noolite.features.home

sealed interface HomeSideEffect {
    data class Message(val message: String) : HomeSideEffect
}
