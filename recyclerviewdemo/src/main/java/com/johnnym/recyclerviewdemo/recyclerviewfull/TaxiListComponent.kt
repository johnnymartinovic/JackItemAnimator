package com.johnnym.recyclerviewdemo.recyclerviewfull

import com.johnnym.recyclerviewdemo.common.di.ActivityScoped
import com.johnnym.recyclerviewdemo.recyclerviewfull.presentation.TaxiListActivity
import dagger.Subcomponent

@ActivityScoped
@Subcomponent(
        modules = [(TaxiListModule::class)])
interface TaxiListComponent {

    fun inject(taxiListActivity: TaxiListActivity)
}
