package com.enxy.noolite.core.utils.extension

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

inline fun <reified T : ViewModel> getViewModel(
    fragment: Fragment,
    factory: ViewModelProvider.Factory
) =
    ViewModelProvider(fragment, factory)[T::class.java]

