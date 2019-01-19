package com.johnnym.jackitemanimator.sample.travelino

import com.johnnym.jackitemanimator.sample.travelino.data.TravelinoListRepository
import com.johnnym.jackitemanimator.sample.travelino.data.MockTravelinoListRepository
import com.johnnym.jackitemanimator.sample.travelino.domain.GetTravelinoList
import com.johnnym.jackitemanimator.sample.travelino.presentation.TravelinoContract
import com.johnnym.jackitemanimator.sample.travelino.presentation.TravelinoListViewModelMapper
import com.johnnym.jackitemanimator.sample.travelino.presentation.TravelinoPresenter
import dagger.Module
import dagger.Provides
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@Module
class TravelinoModule(
        private val travelinoView: TravelinoContract.View
) {

    @Provides
    fun provideTravelinoContractPresenter(
            getTravelinoList: GetTravelinoList,
            travelinoListViewModelMapper: TravelinoListViewModelMapper
    ): TravelinoContract.Presenter =
            TravelinoPresenter(
                    travelinoView,
                    getTravelinoList,
                    travelinoListViewModelMapper)

    @Provides
    fun provideGetTravelinoList(
            travelinoListRepository: TravelinoListRepository
    ): GetTravelinoList =
            GetTravelinoList(
                    travelinoListRepository,
                    Schedulers.io(),
                    AndroidSchedulers.mainThread())

    @Provides
    fun provideTravelinoListRepository(): TravelinoListRepository =
            MockTravelinoListRepository()

    @Provides
    fun provideTravelinoListViewModelMapper(): TravelinoListViewModelMapper =
            TravelinoListViewModelMapper()
}
