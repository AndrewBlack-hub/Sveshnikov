package com.example.tinkoffapp

import android.app.Application
import com.example.tinkoffapp.di.injectModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@App)
            injectModules()
        }
    }
}