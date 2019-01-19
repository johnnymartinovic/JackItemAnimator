package com.johnnym.jackitemanimator.sample.travelino.presentation

import com.johnnym.jackitemanimator.sample.common.mvp.AbsPresenter
import com.johnnym.jackitemanimator.sample.common.presentation.GeneralSingleObserver
import com.johnnym.jackitemanimator.sample.travelino.domain.GetTravelinoList
import com.johnnym.jackitemanimator.sample.travelino.domain.TravelinoList

class TravelinoPresenter(
        private val travelinoView: TravelinoContract.View,
        private val getTravelinoList: GetTravelinoList,
        private val travelinoListViewModelMapper: TravelinoListViewModelMapper
) : AbsPresenter(), TravelinoContract.Presenter {

    init {
        getAndShowTravelinoList()
    }

    override fun onRefreshButtonPressed() {
        getAndShowTravelinoList()
    }

    private fun getAndShowTravelinoList() {
        travelinoView.showLoading()

        getTravelinoList.disposePendingExecutions()
        getTravelinoList.execute(
                GetTravelinoListObserver(),
                GetTravelinoList.Params())
    }

    inner class GetTravelinoListObserver : GeneralSingleObserver<TravelinoList>(this) {

        override fun onSuccess(travelinoList: TravelinoList) {
            travelinoView.hideLoading()
            travelinoView.showTravelinoListViewModel(travelinoListViewModelMapper.map(travelinoList))
        }

        override fun onError(e: Throwable?) {
            travelinoView.hideLoading()
        }
    }
}
