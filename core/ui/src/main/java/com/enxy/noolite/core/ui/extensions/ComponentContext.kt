package com.enxy.noolite.core.ui.extensions

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

fun ComponentContext.componentScope(
    dispatcher: CoroutineDispatcher = Dispatchers.Main.immediate,
): CoroutineScope {
    val scope = CoroutineScope(dispatcher + SupervisorJob())
    lifecycle.doOnDestroy(scope::cancel)
    return scope
}
