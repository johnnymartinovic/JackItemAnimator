package com.johnnym.jackitemanimator.sample.greentuesday.presentation

import com.johnnym.jackitemanimator.sample.common.mvp.AbsPresenter

class GreenTuesdayPresenter(
        private val greenTuesdayView: GreenTuesdayContract.View
) : AbsPresenter(), GreenTuesdayContract.Presenter
