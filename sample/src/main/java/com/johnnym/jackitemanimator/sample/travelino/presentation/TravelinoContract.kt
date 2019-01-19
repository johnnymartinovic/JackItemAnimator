package com.johnnym.jackitemanimator.sample.travelino.presentation

import com.johnnym.jackitemanimator.sample.common.mvp.BasePresenter

interface TravelinoContract {

    interface View {

        fun showTravelinoListViewModel(viewModel: TravelinoListViewModel)

        fun showLoading()

        fun hideLoading()
    }

    interface Presenter : BasePresenter {

        fun onRefreshButtonPressed()
    }
}
