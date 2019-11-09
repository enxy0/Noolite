package com.enxy.noolite

import android.app.Application
import com.enxy.noolite.core.di.ApplicationComponent
import com.enxy.noolite.core.di.ApplicationModule
import com.enxy.noolite.core.di.DaggerApplicationComponent

class AndroidApplication : Application() {

    val appComponent: ApplicationComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        DaggerApplicationComponent
            .builder()
            .applicationModule(ApplicationModule(this))
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        appComponent.inject(this)
    }
}