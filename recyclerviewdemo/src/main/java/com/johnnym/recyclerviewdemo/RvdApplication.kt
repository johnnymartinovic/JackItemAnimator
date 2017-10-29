package com.johnnym.recyclerviewdemo

import android.app.Application

class RvdApplication : Application() {

    lateinit var rvdApplicationComponent: RvdApplicationComponent

    override fun onCreate() {
        super.onCreate()
        rvdApplicationComponent = DaggerRvdApplicationComponent.builder()
                .build()
        rvdApplicationComponent.inject(this)
    }
}