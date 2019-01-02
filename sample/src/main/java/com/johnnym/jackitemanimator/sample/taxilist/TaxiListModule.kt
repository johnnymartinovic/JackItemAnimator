package com.johnnym.jackitemanimator.sample.taxilist

import com.johnnym.jackitemanimator.sample.taxilist.data.MockTaxiListRepository
import com.johnnym.jackitemanimator.sample.taxilist.data.TaxiListRepository
import com.johnnym.jackitemanimator.sample.taxilist.domain.GetTaxiList
import com.johnnym.jackitemanimator.sample.taxilist.domain.TaxiSortOption
import com.johnnym.jackitemanimator.sample.taxilist.domain.TaxiStatusFilter
import com.johnnym.jackitemanimator.sample.taxilist.presentation.TaxiListContract
import com.johnnym.jackitemanimator.sample.taxilist.presentation.TaxiListViewModelMapper
import com.johnnym.jackitemanimator.sample.taxilist.presentation.TaxiListPresenter
import dagger.Module
import dagger.Provides
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@Module
class TaxiListModule(
        private val taxiListView: TaxiListContract.View,
        private var initialTaxiStatusFilter : TaxiStatusFilter,
        private var initialTaxiSortOption : TaxiSortOption?) {

    @Provides
    fun provideTaxiListPresenter(
            getTaxiList: GetTaxiList,
            taxiListViewModelMapper: TaxiListViewModelMapper
    ): TaxiListContract.Presenter =
            TaxiListPresenter(
                    taxiListView,
                    getTaxiList,
                    taxiListViewModelMapper,
                    initialTaxiStatusFilter,
                    initialTaxiSortOption)

    @Provides
    fun provideGetTaxiList(
            taxiListRepository: TaxiListRepository
    ): GetTaxiList =
            GetTaxiList(
                    taxiListRepository,
                    Schedulers.io(),
                    AndroidSchedulers.mainThread())

    @Provides
    fun provideTaxiListRepository(): TaxiListRepository =
            MockTaxiListRepository()

    @Provides
    fun provideTaxiListViewModelMapper(): TaxiListViewModelMapper =
            TaxiListViewModelMapper()
}
