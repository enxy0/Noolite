package com.enxy.noolite.domain.home.di

import com.enxy.noolite.domain.home.GetFavoriteGroupUseCase
import com.enxy.noolite.domain.home.GetFavoriteGroupUseCaseImpl
import com.enxy.noolite.domain.home.GetHomeDataUseCase
import com.enxy.noolite.domain.home.GetHomeDataUseCaseImpl
import com.enxy.noolite.domain.home.GroupActionUseCase
import com.enxy.noolite.domain.home.GroupActionUseCaseImpl
import com.enxy.noolite.domain.home.SetFavoriteGroupUseCase
import com.enxy.noolite.domain.home.SetFavoriteGroupUseCaseImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

object DomainHomeModules {

    fun all(): List<Module> = listOf(
        useCaseModule(),
    )

    private fun useCaseModule(): Module = module {
        factoryOf(::GetFavoriteGroupUseCaseImpl) bind GetFavoriteGroupUseCase::class
        factoryOf(::GetHomeDataUseCaseImpl) bind GetHomeDataUseCase::class
        factoryOf(::GroupActionUseCaseImpl) bind GroupActionUseCase::class
        factoryOf(::SetFavoriteGroupUseCaseImpl) bind SetFavoriteGroupUseCase::class
    }
}
