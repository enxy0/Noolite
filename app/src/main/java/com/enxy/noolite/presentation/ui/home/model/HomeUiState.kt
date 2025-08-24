package com.enxy.noolite.presentation.ui.home.model

import com.enxy.noolite.domain.features.home.model.HomeData

sealed class HomeUiState {
    object Initial : HomeUiState()
    data class Empty(val apiUrl: String, val isLoading: Boolean) : HomeUiState()
    data class Content(val data: HomeData) : HomeUiState()
}
