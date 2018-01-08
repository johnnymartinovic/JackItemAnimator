package com.johnnym.recyclerviewdemo.recyclerviewfull.presentation

import com.johnnym.recyclerviewdemo.common.mvp.BasePresenter

interface TaxiListContract {

    interface View {

        fun showTaxiListPresentable(taxiListPresentable: TaxiListPresentable)

        fun showLoading()

        fun hideLoading()
    }

    interface Presenter: BasePresenter {

        fun availabilityVisibilitySwitchChecked(checked: Boolean)
    }
}
