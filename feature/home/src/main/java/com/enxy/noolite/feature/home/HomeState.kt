package com.enxy.noolite.feature.home

import com.enxy.noolite.domain.home.model.HomeData

sealed interface HomeState {
    data object Initial : HomeState

    data class Empty(
        val apiUrl: String,
        val isLoading: Boolean,
    ) : HomeState

    data class Content(
        val data: HomeData,
    ) : HomeState
}

fun HomeState.toContentKey(): Int = when (this) {
    is HomeState.Initial -> 0
    is HomeState.Empty -> 1
    is HomeState.Content -> 2
}
