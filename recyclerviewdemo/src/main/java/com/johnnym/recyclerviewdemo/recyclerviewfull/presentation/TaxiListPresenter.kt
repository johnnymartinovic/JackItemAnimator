package com.johnnym.recyclerviewdemo.recyclerviewfull.presentation

import com.johnnym.recyclerviewdemo.common.mvp.AbsPresenter
import com.johnnym.recyclerviewdemo.common.presentation.GeneralSingleObserver
import com.johnnym.recyclerviewdemo.recyclerviewfull.domain.GetTaxiList
import com.johnnym.recyclerviewdemo.recyclerviewfull.domain.TaxiList
import com.johnnym.recyclerviewdemo.recyclerviewfull.domain.TaxiStatusFilter

class TaxiListPresenter(
        private val taxiListView: TaxiListContract.View,
        private val getTaxiList: GetTaxiList,
        private val taxiListPresentableMapper: TaxiListPresentableMapper
) : AbsPresenter(), TaxiListContract.Presenter {

    private var currentTaxiStatusFilter = TaxiStatusFilter.ONLY_AVAILABLE

    init {
        getAndShowTaxiList()
    }

    override fun availabilityVisibilitySwitchChecked(checked: Boolean) {
        currentTaxiStatusFilter =
                if (checked) TaxiStatusFilter.NO_FILTER
                else TaxiStatusFilter.ONLY_AVAILABLE

        getAndShowTaxiList()
    }

    private fun getAndShowTaxiList() {
        taxiListView.showLoading()

        getTaxiList.disposePendingExecutions()
        getTaxiList.execute(
                GetTaxiListObserver(),
                GetTaxiList.Params(currentTaxiStatusFilter))
    }

    inner class GetTaxiListObserver : GeneralSingleObserver<TaxiList>(this) {

        override fun onSuccess(taxiList: TaxiList) {
            taxiListView.hideLoading()
            taxiListView.showTaxiListPresentable(
                    taxiListPresentableMapper.map(taxiList))
        }

        override fun onError(e: Throwable?) {
            taxiListView.hideLoading()
        }
    }
}