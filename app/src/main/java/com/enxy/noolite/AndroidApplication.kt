package com.enxy.noolite

import android.app.Application
import com.enxy.noolite.core.di.appModule
import com.enxy.noolite.core.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class AndroidApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AndroidApplication)
            androidLogger(Level.DEBUG)
            modules(listOf(appModule, networkModule))
        }
    }
}