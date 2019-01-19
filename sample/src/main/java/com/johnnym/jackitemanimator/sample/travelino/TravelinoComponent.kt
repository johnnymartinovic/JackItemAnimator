package com.johnnym.jackitemanimator.sample.travelino

import com.johnnym.jackitemanimator.sample.common.di.ActivityScoped
import com.johnnym.jackitemanimator.sample.travelino.presentation.TravelinoActivity
import dagger.Subcomponent

@ActivityScoped
@Subcomponent(
        modules = [(TravelinoModule::class)])
interface TravelinoComponent {

    fun inject(travelinoActivity: TravelinoActivity)
}
