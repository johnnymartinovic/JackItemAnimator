package com.johnnym.jackitemanimator.sample.taxilist

import com.johnnym.jackitemanimator.sample.common.di.ActivityScoped
import com.johnnym.jackitemanimator.sample.taxilist.presentation.TaxiListActivity
import dagger.Subcomponent

@ActivityScoped
@Subcomponent(
        modules = [(TaxiListModule::class)])
interface TaxiListComponent {

    fun inject(taxiListActivity: TaxiListActivity)
}
