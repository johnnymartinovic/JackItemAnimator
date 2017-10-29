package com.johnnym.recyclerviewdemo.recyclerviewfull

import dagger.Module
import dagger.Provides

@Module
class TaxiListModule(private val taxiListView: TaxiListContract.View) {

    @Provides
    fun provideTaxiListPresenter(): TaxiListContract.Presenter =
            TaxiListPresenter(taxiListView)
}
