package com.enxy.noolite.di

import com.enxy.noolite.core.database.di.CoreDatabaseModules
import com.enxy.noolite.core.network.di.CoreNetworkModules
import com.enxy.noolite.data.home.di.DataHomeModules
import com.enxy.noolite.data.script.di.DataScriptModules
import com.enxy.noolite.data.settings.di.DataSettingsModules
import com.enxy.noolite.domain.home.di.DomainHomeModules
import com.enxy.noolite.domain.script.di.DomainScriptModules
import com.enxy.noolite.domain.settings.di.DomainSettingsModules
import org.koin.dsl.module

object KoinModules {
    val main = module {
        includes(PresentationModules.all())
        includes(CoreNetworkModules.all())
        includes(CoreDatabaseModules.all())
        includes(DataSettingsModules.all())
        includes(DataScriptModules.all())
        includes(DataScriptModules.all())
        includes(DataHomeModules.all())
        includes(DomainHomeModules.all())
        includes(DomainScriptModules.all())
        includes(DomainSettingsModules.all())
    }
}
