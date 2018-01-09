package com.johnnym.recyclerviewdemo.recyclerviewfull.presentation

import com.johnnym.recyclerviewdemo.common.mvp.AbsPresenter
import com.johnnym.recyclerviewdemo.common.presentation.GeneralSingleObserver
import com.johnnym.recyclerviewdemo.recyclerviewfull.domain.GetTaxiList
import com.johnnym.recyclerviewdemo.recyclerviewfull.domain.TaxiList
import com.johnnym.recyclerviewdemo.recyclerviewfull.domain.TaxiStatusFilter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class TaxiListPresenter(
        private val taxiListView: TaxiListContract.View,
        private val getTaxiList: GetTaxiList,
        private val taxiListPresentableMapper: TaxiListPresentableMapper
) : AbsPresenter(), TaxiListContract.Presenter {

    private var currentTaxiStatusFilter = TaxiStatusFilter.ONLY_AVAILABLE
    private var currentTaxiSortOption = null

    init {
        Observable
                .interval(0, 6, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { getAndShowTaxiList() }
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
                GetTaxiList.Params(currentTaxiStatusFilter, currentTaxiSortOption))
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