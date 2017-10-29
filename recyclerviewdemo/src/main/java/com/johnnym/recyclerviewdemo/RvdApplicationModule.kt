package com.johnnym.recyclerviewdemo

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RvdApplicationModule(private val rvdApplication: RvdApplication) {

    @Provides
    @Singleton
    fun rvdApplication(): RvdApplication {
        return rvdApplication
    }

    @Provides
    @Singleton
    fun context(): Context {
        return rvdApplication
    }
}