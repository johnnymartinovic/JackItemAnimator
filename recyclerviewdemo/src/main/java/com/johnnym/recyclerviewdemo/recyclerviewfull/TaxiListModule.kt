package com.johnnym.recyclerviewdemo.recyclerviewfull

import com.johnnym.recyclerviewdemo.recyclerviewfull.data.MockTaxiListRepository
import com.johnnym.recyclerviewdemo.recyclerviewfull.data.TaxiListRepository
import com.johnnym.recyclerviewdemo.recyclerviewfull.domain.GetTaxiList
import com.johnnym.recyclerviewdemo.recyclerviewfull.presentation.TaxiListContract
import com.johnnym.recyclerviewdemo.recyclerviewfull.presentation.TaxiListPresentableMapper
import com.johnnym.recyclerviewdemo.recyclerviewfull.presentation.TaxiListPresenter
import dagger.Module
import dagger.Provides
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@Module
class TaxiListModule(
        private val taxiListView: TaxiListContract.View) {

    @Provides
    fun provideTaxiListPresenter(
            getTaxiList: GetTaxiList,
            taxiListPresentableMapper: TaxiListPresentableMapper
    ): TaxiListContract.Presenter =
            TaxiListPresenter(
                    taxiListView,
                    getTaxiList,
                    taxiListPresentableMapper)

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
    fun provideTaxiListPresentableMapper(): TaxiListPresentableMapper =
            TaxiListPresentableMapper()
}
