package com.johnnym.jackitemanimator.sample

import android.app.Application
import com.squareup.leakcanary.LeakCanary
import timber.log.Timber

class SampleApplication : Application() {

    lateinit var sampleApplicationComponent: SampleApplicationComponent

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }
        LeakCanary.install(this)

        sampleApplicationComponent = DaggerSampleApplicationComponent.builder()
                .build()
        sampleApplicationComponent.inject(this)
    }
}
