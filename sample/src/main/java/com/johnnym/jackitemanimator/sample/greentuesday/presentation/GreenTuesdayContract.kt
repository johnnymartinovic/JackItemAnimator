package com.johnnym.jackitemanimator.sample.greentuesday.presentation

import com.johnnym.jackitemanimator.sample.common.mvp.BasePresenter

interface GreenTuesdayContract {

    interface View {

        fun showGreenTuesdayListViewModel(viewModel: GreenTuesdayListViewModel)

        fun showLoading()

        fun hideLoading()

        fun showSortOptionsDialog(
                sortOptionList: List<String>,
                initiallySelectedSortOptionPosition: Int)
    }

    interface Presenter : BasePresenter {

        fun onSortButtonPressed()

        fun onSortOptionSelected(selectedSortOptionPosition: Int)

        fun onRefreshButtonPressed()
    }
}
