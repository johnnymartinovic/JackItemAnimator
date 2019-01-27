package com.johnnym.jackitemanimator.sample.travelino.data

import com.johnnym.jackitemanimator.sample.travelino.domain.TravelinoList
import io.reactivex.Single
import java.util.concurrent.TimeUnit

class MockTravelinoListRepository : TravelinoListRepository {

    private var nextInstanceNumber = 0

    override fun getTravelinoListSingle(): Single<TravelinoList> {
        return Single
                .create<TravelinoList> {
                    val list = TravelinoMockFactory.createTravelinoItemList(nextInstanceNumber)

                    nextInstanceNumber = (nextInstanceNumber + 1) % 2

                    it.onSuccess(TravelinoList(list))
                }
                .delay(1, TimeUnit.SECONDS)
    }
}
