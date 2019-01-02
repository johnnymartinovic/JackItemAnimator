package com.johnnym.jackitemanimator.sample

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class SampleApplicationModule(
        private val sampleApplication: SampleApplication
) {

    @Provides
    @Singleton
    fun sampleApplication(): SampleApplication {
        return sampleApplication
    }

    @Provides
    @Singleton
    fun context(): Context {
        return sampleApplication
    }
}
