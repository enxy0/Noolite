package com.enxy.noolite.di.modules

import com.enxy.noolite.presentation.ui.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

internal val homeModule = module {
    viewModelOf(::HomeViewModel)
}
