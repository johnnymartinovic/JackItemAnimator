package com.johnnym.recyclerviewdemo

import android.app.Application
import com.squareup.leakcanary.LeakCanary
import timber.log.Timber

class RvdApplication : Application() {

    lateinit var rvdApplicationComponent: RvdApplicationComponent

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }
        LeakCanary.install(this)

        rvdApplicationComponent = DaggerRvdApplicationComponent.builder()
                .build()
        rvdApplicationComponent.inject(this)
    }
}