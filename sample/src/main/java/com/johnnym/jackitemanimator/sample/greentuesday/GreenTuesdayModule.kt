package com.johnnym.jackitemanimator.sample.greentuesday

import com.johnnym.jackitemanimator.sample.greentuesday.data.GreenTuesdayListRepository
import com.johnnym.jackitemanimator.sample.greentuesday.data.MockGreenTuesdayListRepository
import com.johnnym.jackitemanimator.sample.greentuesday.domain.GetGreenTuesdayList
import com.johnnym.jackitemanimator.sample.greentuesday.domain.GreenTuesdayListSortOption
import com.johnnym.jackitemanimator.sample.greentuesday.presentation.GreenTuesdayContract
import com.johnnym.jackitemanimator.sample.greentuesday.presentation.GreenTuesdayListViewModelMapper
import com.johnnym.jackitemanimator.sample.greentuesday.presentation.GreenTuesdayPresenter
import dagger.Module
import dagger.Provides
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@Module
class GreenTuesdayModule(
        private val greenTuesdayView: GreenTuesdayContract.View,
        private var initialGreenTuesdayListSortOption: GreenTuesdayListSortOption?
) {

    @Provides
    fun provideGreenTuesdayContractPresenter(
            getGreenTuesdayList: GetGreenTuesdayList,
            greenTuesdayListViewModelMapper: GreenTuesdayListViewModelMapper
    ): GreenTuesdayContract.Presenter =
            GreenTuesdayPresenter(
                    greenTuesdayView,
                    getGreenTuesdayList,
                    greenTuesdayListViewModelMapper,
                    initialGreenTuesdayListSortOption)

    @Provides
    fun provideGetGreenTuesdayList(
            greenTuesdayListRepository: GreenTuesdayListRepository
    ): GetGreenTuesdayList =
            GetGreenTuesdayList(
                    greenTuesdayListRepository,
                    Schedulers.io(),
                    AndroidSchedulers.mainThread())

    @Provides
    fun provideGreenTuesdayListRepository(): GreenTuesdayListRepository =
            MockGreenTuesdayListRepository()

    @Provides
    fun provideGreenTuesdayListViewModelMapper(): GreenTuesdayListViewModelMapper =
            GreenTuesdayListViewModelMapper()
}
