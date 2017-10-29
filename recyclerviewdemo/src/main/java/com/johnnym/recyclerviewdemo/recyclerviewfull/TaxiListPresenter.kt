package com.johnnym.recyclerviewdemo.recyclerviewfull

class TaxiListPresenter(taxiListView: TaxiListContract.View) : TaxiListContract.Presenter {

    init {
        taxiListView.showTaxiListPresentable(
                TaxiListPresentable(mutableListOf(
                        TaxiListItemPresentable(
                                TaxiStatus.AVAILABLE,
                                "https://pbs.twimg.com/media/CO0d2ZcUkAA6gjw.jpg",
                                "Brad Pitt",
                                3.545f,
                                2.7433f),
                        TaxiListItemPresentable(
                                TaxiStatus.OCCUPIED,
                                "https://vignette.wikia.nocookie.net/erwikia/images/c/c8/George_Clooney.jpg/revision/latest?cb=20120812171039",
                                "George Clooney",
                                4.23f,
                                8.644f)
                )))
    }

    override fun visibilitySwitchChecked(checked: Boolean) {

    }
}