package com.naumenko.rickandmorty

import android.app.Application
import android.content.Context
import com.naumenko.rickandmorty.di.AppComponent
import com.naumenko.rickandmorty.di.DaggerAppComponent

class MainApplication : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
            .application(this)
            .build()
    }
}

val Context.appComponent: AppComponent
    get() = when (this) {
        is MainApplication -> this.appComponent
        else -> this.applicationContext.appComponent
    }