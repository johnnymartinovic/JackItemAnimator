package com.johnnym.recyclerviewdemo.recyclerviewfull.presentation

import com.johnnym.recyclerviewdemo.common.mvp.AbsPresenter
import com.johnnym.recyclerviewdemo.common.presentation.GeneralSingleObserver
import com.johnnym.recyclerviewdemo.recyclerviewfull.domain.GetTaxiList
import com.johnnym.recyclerviewdemo.recyclerviewfull.domain.TaxiList
import com.johnnym.recyclerviewdemo.recyclerviewfull.domain.TaxiSortOption
import com.johnnym.recyclerviewdemo.recyclerviewfull.domain.TaxiStatusFilter

class TaxiListPresenter(
        private val taxiListView: TaxiListContract.View,
        private val getTaxiList: GetTaxiList,
        private val taxiListViewModelMapper: TaxiListViewModelMapper,
        private var currentTaxiStatusFilter: TaxiStatusFilter,
        private var currentTaxiSortOption: TaxiSortOption
) : AbsPresenter(), TaxiListContract.Presenter {

    init {
        getAndShowTaxiList()
    }

    override fun availabilityVisibilitySwitchChecked(checked: Boolean) {
        currentTaxiStatusFilter =
                if (checked) TaxiStatusFilter.NO_FILTER
                else TaxiStatusFilter.ONLY_AVAILABLE

        getAndShowTaxiList()
    }

    override fun onSortButtonPressed() {
        taxiListView.showSortOptionsDialog(
                TaxiSortOption.values()
                        .map { it.getTaxiSortOptionName() },
                currentTaxiSortOption.ordinal)
    }

    override fun onSortOptionSelected(selectedSortOptionPosition: Int) {
        currentTaxiSortOption = TaxiSortOption.values()[selectedSortOptionPosition]

        getAndShowTaxiList()
    }

    override fun onRefreshButtonPressed() {
        getAndShowTaxiList()
    }

    private fun getAndShowTaxiList() {
        taxiListView.showLoading()

        getTaxiList.disposePendingExecutions()
        getTaxiList.execute(
                GetTaxiListObserver(),
                GetTaxiList.Params(currentTaxiStatusFilter, currentTaxiSortOption))
    }

    inner class GetTaxiListObserver : GeneralSingleObserver<TaxiList>(this) {

        override fun onSuccess(taxiList: TaxiList) {
            taxiListView.hideLoading()
            taxiListView.showTaxiListViewModel(taxiListViewModelMapper.map(taxiList))
        }

        override fun onError(e: Throwable?) {
            taxiListView.hideLoading()
        }
    }
}