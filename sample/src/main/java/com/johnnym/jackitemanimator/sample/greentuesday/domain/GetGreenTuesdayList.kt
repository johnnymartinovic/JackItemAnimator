package com.johnnym.jackitemanimator.sample.greentuesday.domain

import com.johnnym.jackitemanimator.sample.common.domain.UseCase
import com.johnnym.jackitemanimator.sample.greentuesday.data.GreenTuesdayListRepository
import io.reactivex.Scheduler
import io.reactivex.Single

class GetGreenTuesdayList(
        private val greenTuesdayListRepository: GreenTuesdayListRepository,
        subscribeOnScheduler: Scheduler,
        observeOnScheduler: Scheduler
) : UseCase<GreenTuesdayList, GetGreenTuesdayList.Params>(subscribeOnScheduler, observeOnScheduler) {

    override fun buildUseCase(params: Params): Single<GreenTuesdayList> {
        val greenTuesdayListSingle = greenTuesdayListRepository.getGreenTuesdayListSingle()

        return if (params.sortOption == null) greenTuesdayListSingle
        else greenTuesdayListSingle.map {
            GreenTuesdayList(it.listItems
                    .sortedWith(params.sortOption.createComparator()))
        }
    }

    class Params(
            val sortOption: GreenTuesdayListSortOption? = null)
}
