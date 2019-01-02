package com.johnnym.jackitemanimator.sample

import com.johnnym.jackitemanimator.sample.taxilist.TaxiListComponent
import com.johnnym.jackitemanimator.sample.taxilist.TaxiListModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
        modules = [(RvdApplicationModule::class)]
)
interface RvdApplicationComponent {

    fun inject(rvdApplication: RvdApplication)

    fun newTaxiListComponent(taxiListModule: TaxiListModule): TaxiListComponent
}