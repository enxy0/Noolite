package com.enxy.noolite.feature.settings.url

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.statekeeper.ExperimentalStateKeeperApi
import org.koin.core.component.KoinComponent

internal interface ChangeApiUrlComponent {
    val currentApiUrl: String
    fun onDismiss()
    fun onApplyClick(apiUrl: String)
}

@OptIn(ExperimentalStateKeeperApi::class)
internal class ChangeApiUrlComponentImpl(
    componentContext: ComponentContext,
    override val currentApiUrl: String,
    private val onDismissed: () -> Unit,
    private val onUrlChanged: (apiUrl: String) -> Unit,
) : ChangeApiUrlComponent,
    ComponentContext by componentContext,
    KoinComponent {


    override fun onDismiss() {
        onDismissed()
    }

    override fun onApplyClick(apiUrl: String) {
        onUrlChanged(apiUrl)
    }
}
