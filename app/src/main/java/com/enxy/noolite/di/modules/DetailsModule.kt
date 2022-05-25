package com.enxy.noolite.di.modules

import com.enxy.noolite.presentation.ui.detail.DetailsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val detailsModule = module {
    viewModel { params -> DetailsViewModel(params.get(), get(), get(), get()) }
}
