package com.johnnym.recyclerviewdemo.recyclerviewfull.presentation

import com.johnnym.recyclerviewdemo.common.mvp.BasePresenter

interface TaxiListContract {

    interface View {

        fun showTaxiListPresentable(taxiListPresentable: TaxiListPresentable)

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
    }
}
