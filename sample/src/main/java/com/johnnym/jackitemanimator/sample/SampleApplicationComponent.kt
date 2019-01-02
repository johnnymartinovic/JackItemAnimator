package com.johnnym.jackitemanimator.sample

import com.johnnym.jackitemanimator.sample.taxilist.TaxiListComponent
import com.johnnym.jackitemanimator.sample.taxilist.TaxiListModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
        modules = [(SampleApplicationModule::class)]
)
interface SampleApplicationComponent {

    fun inject(sampleApplication: SampleApplication)

    fun newTaxiListComponent(taxiListModule: TaxiListModule): TaxiListComponent
}
