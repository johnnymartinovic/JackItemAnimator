package com.johnnym.recyclerviewdemo

import com.johnnym.recyclerviewdemo.recyclerviewfull.TaxiListComponent
import com.johnnym.recyclerviewdemo.recyclerviewfull.TaxiListModule
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