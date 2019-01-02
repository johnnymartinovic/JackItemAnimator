package com.johnnym.jackitemanimator.sample.taxilist.presentation

import com.johnnym.jackitemanimator.sample.common.mvp.BasePresenter

interface TaxiListContract {

    interface View {

        fun showTaxiListViewModel(taxiListViewModel: TaxiListViewModel)

        fun showLoading()

        fun hideLoading()

        fun showSortOptionsDialog(
                sortOptionList: List<String>,
                initiallySelectedSortOptionPosition: Int)
    }

    interface Presenter: BasePresenter {

        fun availabilityVisibilitySwitchChecked(checked: Boolean)

        fun onSortButtonPressed()

        fun onSortOptionSelected(selectedSortOptionPosition: Int)

        fun onRefreshButtonPressed()
    }
}
