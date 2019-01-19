package com.johnnym.jackitemanimator.sample.travelino.data

import com.johnnym.jackitemanimator.sample.travelino.domain.TravelinoList
import io.reactivex.Single

interface TravelinoListRepository {

    fun getTravelinoListSingle(): Single<TravelinoList>
}
