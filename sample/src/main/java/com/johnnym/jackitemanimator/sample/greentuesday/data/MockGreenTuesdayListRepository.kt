package com.johnnym.jackitemanimator.sample.greentuesday.data

import com.johnnym.jackitemanimator.sample.greentuesday.domain.GreenTuesdayList
import io.reactivex.Single
import java.util.concurrent.TimeUnit

class MockGreenTuesdayListRepository : GreenTuesdayListRepository {

    private var getGreenTuesdayListRequestNumber = 0

    override fun getGreenTuesdayListSingle(): Single<GreenTuesdayList> {
        return Single
                .create<GreenTuesdayList> {
                    it.onSuccess(GreenTuesdayMockFactory.createGreenTuesdayList(getGreenTuesdayListRequestNumber))
                }
                .delay(1, TimeUnit.SECONDS)
    }
}
