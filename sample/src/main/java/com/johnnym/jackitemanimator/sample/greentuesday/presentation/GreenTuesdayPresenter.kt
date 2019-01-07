package com.johnnym.jackitemanimator.sample.greentuesday.presentation

import com.johnnym.jackitemanimator.sample.common.mvp.AbsPresenter
import com.johnnym.jackitemanimator.sample.common.presentation.GeneralSingleObserver
import com.johnnym.jackitemanimator.sample.greentuesday.domain.GetGreenTuesdayList
import com.johnnym.jackitemanimator.sample.greentuesday.domain.GreenTuesdayList

class GreenTuesdayPresenter(
        private val greenTuesdayView: GreenTuesdayContract.View,
        private val getGreenTuesdayList: GetGreenTuesdayList,
        private val greenTuesdayListViewModelMapper: GreenTuesdayListViewModelMapper
) : AbsPresenter(), GreenTuesdayContract.Presenter {

    init {
        getAndShowGreenTuesdayList()
    }

    override fun onRefreshButtonPressed() {
        getAndShowGreenTuesdayList()
    }

    private fun getAndShowGreenTuesdayList() {
        greenTuesdayView.showLoading()

        getGreenTuesdayList.disposePendingExecutions()
        getGreenTuesdayList.execute(
                GetGreenTuesdayListObserver(),
                GetGreenTuesdayList.Params())
    }

    inner class GetGreenTuesdayListObserver : GeneralSingleObserver<GreenTuesdayList>(this) {

        override fun onSuccess(greenTuesdayList: GreenTuesdayList) {
            greenTuesdayView.hideLoading()
            greenTuesdayView.showGreenTuesdayListViewModel(greenTuesdayListViewModelMapper.map(greenTuesdayList))
        }

        override fun onError(e: Throwable?) {
            greenTuesdayView.hideLoading()
        }
    }
}
