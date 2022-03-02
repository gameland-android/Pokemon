package com.test.pokemon

import android.app.Application
import com.test.pokemon.di.apiModule
import com.test.pokemon.di.dataFlowModule
import com.test.pokemon.di.repositoryModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApplication)
            modules(dataFlowModule + repositoryModule + apiModule)
        }
    }

}