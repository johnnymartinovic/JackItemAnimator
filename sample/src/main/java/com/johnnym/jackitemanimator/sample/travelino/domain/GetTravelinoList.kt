package com.johnnym.jackitemanimator.sample.travelino.domain

import com.johnnym.jackitemanimator.sample.common.domain.UseCase
import com.johnnym.jackitemanimator.sample.travelino.data.TravelinoListRepository
import io.reactivex.Scheduler
import io.reactivex.Single

class GetTravelinoList(
        private val travelinoListRepository: TravelinoListRepository,
        subscribeOnScheduler: Scheduler,
        observeOnScheduler: Scheduler
) : UseCase<TravelinoList, GetTravelinoList.Params>(subscribeOnScheduler, observeOnScheduler) {

    override fun buildUseCase(params: Params): Single<TravelinoList> =
            travelinoListRepository.getTravelinoListSingle()

    class Params
}
