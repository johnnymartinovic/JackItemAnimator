package com.johnnym.jackitemanimator.sample.taxilist.presentation

import com.johnnym.jackitemanimator.sample.common.mvp.AbsPresenter
import com.johnnym.jackitemanimator.sample.common.presentation.GeneralSingleObserver
import com.johnnym.jackitemanimator.sample.taxilist.domain.GetTaxiList
import com.johnnym.jackitemanimator.sample.taxilist.domain.TaxiList
import com.johnnym.jackitemanimator.sample.taxilist.domain.TaxiSortOption
import com.johnnym.jackitemanimator.sample.taxilist.domain.TaxiStatusFilter

class TaxiListPresenter(
        private val taxiListView: TaxiListContract.View,
        private val getTaxiList: GetTaxiList,
        private val taxiListViewModelMapper: TaxiListViewModelMapper,
        private var currentTaxiStatusFilter: TaxiStatusFilter,
        private var currentTaxiSortOption: TaxiSortOption?
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
        val sortOptionList = mutableListOf("Best Match")
        sortOptionList.addAll(TaxiSortOption.values()
                .map { it.getTaxiSortOptionName() })

        taxiListView.showSortOptionsDialog(
                sortOptionList,
                currentTaxiSortOption?.let { it.ordinal + 1 } ?: 0)
    }

    override fun onSortOptionSelected(selectedSortOptionPosition: Int) {
        currentTaxiSortOption =
                if (selectedSortOptionPosition == 0) null
                else TaxiSortOption.values()[selectedSortOptionPosition - 1]

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