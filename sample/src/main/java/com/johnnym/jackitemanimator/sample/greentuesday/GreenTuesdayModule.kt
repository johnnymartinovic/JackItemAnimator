package com.johnnym.jackitemanimator.sample.greentuesday

import com.johnnym.jackitemanimator.sample.greentuesday.presentation.GreenTuesdayContract
import com.johnnym.jackitemanimator.sample.greentuesday.presentation.GreenTuesdayPresenter
import dagger.Module
import dagger.Provides

@Module
class GreenTuesdayModule(
        private val greenTuesdayView: GreenTuesdayContract.View
) {

    @Provides
    fun provideGreenTuesdayContractPresenter(): GreenTuesdayContract.Presenter =
            GreenTuesdayPresenter(greenTuesdayView)
}
