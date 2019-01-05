package com.johnnym.jackitemanimator.sample.greentuesday.presentation

import com.johnnym.jackitemanimator.sample.common.mvp.AbsPresenter
import com.johnnym.jackitemanimator.sample.common.presentation.GeneralSingleObserver
import com.johnnym.jackitemanimator.sample.greentuesday.domain.GetGreenTuesdayList
import com.johnnym.jackitemanimator.sample.greentuesday.domain.GreenTuesdayList
import com.johnnym.jackitemanimator.sample.greentuesday.domain.GreenTuesdayListSortOption

class GreenTuesdayPresenter(
        private val greenTuesdayView: GreenTuesdayContract.View,
        private val getGreenTuesdayList: GetGreenTuesdayList,
        private val greenTuesdayListViewModelMapper: GreenTuesdayListViewModelMapper,
        private var currentGreenTuesdayListSortOption: GreenTuesdayListSortOption?
) : AbsPresenter(), GreenTuesdayContract.Presenter {

    init {
        getAndShowGreenTuesdayList()
    }

    override fun onSortButtonPressed() {
        val sortOptionList = mutableListOf("Best Match")
        sortOptionList.addAll(GreenTuesdayListSortOption.values()
                .map { it.getSortOptionName() })

        greenTuesdayView.showSortOptionsDialog(
                sortOptionList,
                currentGreenTuesdayListSortOption?.let { it.ordinal + 1 } ?: 0)
    }

    override fun onSortOptionSelected(selectedSortOptionPosition: Int) {
        currentGreenTuesdayListSortOption =
                if (selectedSortOptionPosition == 0) null
                else GreenTuesdayListSortOption.values()[selectedSortOptionPosition - 1]

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
                GetGreenTuesdayList.Params(currentGreenTuesdayListSortOption))
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
