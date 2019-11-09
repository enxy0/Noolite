package com.enxy.noolite.core.di

import com.enxy.noolite.AndroidApplication
import com.enxy.noolite.core.di.viewmodel.ViewModelModule
import com.enxy.noolite.features.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, ViewModelModule::class])
interface ApplicationComponent {
    fun inject(application: AndroidApplication)
    fun inject(mainActivity: MainActivity)
}