package com.johnnym.jackitemanimator.sample

import com.johnnym.jackitemanimator.sample.travelino.TravelinoComponent
import com.johnnym.jackitemanimator.sample.travelino.TravelinoModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
        modules = [(SampleApplicationModule::class)]
)
interface SampleApplicationComponent {

    fun inject(sampleApplication: SampleApplication)

    fun newTravelinoComponent(travelinoModule: TravelinoModule): TravelinoComponent
}
