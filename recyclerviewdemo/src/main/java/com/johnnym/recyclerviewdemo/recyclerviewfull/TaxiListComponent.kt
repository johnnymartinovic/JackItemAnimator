package com.johnnym.recyclerviewdemo.recyclerviewfull

import dagger.Subcomponent

@Subcomponent(
        modules = [(TaxiListModule::class)])
interface TaxiListComponent {

    fun inject(taxiListActivity: TaxiListActivity)
}
