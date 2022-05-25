package com.enxy.noolite.di

import com.enxy.data.di.DataModules
import com.enxy.domain.di.DomainModules
import org.koin.core.module.Module

object KoinModules {
    fun all(): List<Module> = PresentationModules.all() + DomainModules.all() + DataModules.all()
}
