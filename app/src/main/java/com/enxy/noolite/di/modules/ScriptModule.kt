package com.enxy.noolite.di.modules

import com.enxy.noolite.presentation.ui.script.ScriptViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val scriptModule = module {
    viewModelOf(::ScriptViewModel)
}
