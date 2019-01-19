package com.johnnym.jackitemanimator.sample.travelino.data

import com.johnnym.jackitemanimator.sample.travelino.domain.TravelinoList
import io.reactivex.Single
import java.util.concurrent.TimeUnit

class MockTravelinoListRepository : TravelinoListRepository {

    private var getTravelinoListRequestNumber = 0

    override fun getTravelinoListSingle(): Single<TravelinoList> {
        return Single
                .create<TravelinoList> {
                    it.onSuccess(TravelinoMockFactory.createTravelinoList(getTravelinoListRequestNumber++))
                }
                .delay(1, TimeUnit.SECONDS)
    }
}
