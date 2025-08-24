package com.enxy.noolite.di

import com.enxy.noolite.data.di.DataModules
import com.enxy.noolite.domain.di.DomainModules
import org.koin.core.module.Module

object KoinModules {
    fun all(): List<Module> = PresentationModules.all() + DomainModules.all() + DataModules.all()
}
