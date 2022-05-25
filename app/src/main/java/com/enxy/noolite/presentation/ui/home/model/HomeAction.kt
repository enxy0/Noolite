package com.enxy.noolite.presentation.ui.home.model

sealed class HomeAction {
    object None : HomeAction()
    class ShowSnackbar(val message: String, val isFailure: Boolean) : HomeAction()
}
