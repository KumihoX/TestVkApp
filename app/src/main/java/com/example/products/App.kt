package com.example.products

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        // Start Koin
        GlobalContext.startKoin {
            androidContext(this@App)
            modules(appModule)
        }
    }
}