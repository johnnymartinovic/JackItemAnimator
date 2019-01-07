package com.johnnym.jackitemanimator.sample.greentuesday.presentation

import com.johnnym.jackitemanimator.sample.common.mvp.BasePresenter

interface GreenTuesdayContract {

    interface View {

        fun showGreenTuesdayListViewModel(viewModel: GreenTuesdayListViewModel)

        fun showLoading()

        fun hideLoading()
    }

    interface Presenter : BasePresenter {

        fun onRefreshButtonPressed()
    }
}
