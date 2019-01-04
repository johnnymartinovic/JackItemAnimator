package com.johnnym.jackitemanimator.sample.greentuesday.data

import com.johnnym.jackitemanimator.sample.greentuesday.domain.GreenTuesdayList
import io.reactivex.Single

interface GreenTuesdayListRepository {

    fun getGreenTuesdayListSingle(): Single<GreenTuesdayList>
}
