package com.johnnym.jackitemanimator.sample.greentuesday.presentation

import com.johnnym.jackitemanimator.sample.common.mvp.AbsPresenter
import com.johnnym.jackitemanimator.sample.greentuesday.domain.GetGreenTuesdayList
import com.johnnym.jackitemanimator.sample.greentuesday.domain.GreenTuesdayListSortOption

class GreenTuesdayPresenter(
        private val greenTuesdayView: GreenTuesdayContract.View,
        private val getGreenTuesdayList: GetGreenTuesdayList,
        private val greenTuesdayListViewModelMapper: GreenTuesdayListViewModelMapper,
        private var greenTuesdayListSortOption: GreenTuesdayListSortOption?
) : AbsPresenter(), GreenTuesdayContract.Presenter
