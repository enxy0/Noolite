package com.enxy.noolite.feature.home

import androidx.compose.runtime.Immutable
import com.enxy.noolite.domain.home.model.HomeData

@Immutable
sealed interface HomeState {
    @Immutable
    data object Initial : HomeState

    @Immutable
    data class Empty(
        val apiUrl: String,
        val isLoading: Boolean,
    ) : HomeState

    @Immutable
    data class Content(
        val data: HomeData,
    ) : HomeState
}

fun HomeState.toContentKey(): Int = when (this) {
    is HomeState.Initial -> 0
    is HomeState.Empty -> 1
    is HomeState.Content -> 2
}
