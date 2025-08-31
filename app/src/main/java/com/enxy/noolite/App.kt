package com.enxy.noolite

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import com.enxy.noolite.di.KoinModules
import com.enxy.noolite.utils.lifecycle.AppThemeObserver
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class App : Application() {

    private val appThemeObserver by inject<AppThemeObserver>()

    override fun onCreate() {
        super.onCreate()
        setupKoin()
        setupTimber()
        setupProcessObservers()
    }

    private fun setupKoin() {
        startKoin {
            androidContext(this@App)
            androidLogger()
            modules(KoinModules.main)
        }
    }

    private fun setupTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun setupProcessObservers() {
        val lifecycle = ProcessLifecycleOwner.get().lifecycle
        lifecycle.addObserver(appThemeObserver)
    }
}
