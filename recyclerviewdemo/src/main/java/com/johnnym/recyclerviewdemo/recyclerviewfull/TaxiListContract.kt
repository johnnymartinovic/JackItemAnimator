package com.johnnym.recyclerviewdemo.recyclerviewfull

interface TaxiListContract {

    interface View {

        fun showTaxiListPresentable(taxiListPresentable: TaxiListPresentable)
    }

    interface Presenter {

        fun visibilitySwitchChecked(checked: Boolean)
    }
}
