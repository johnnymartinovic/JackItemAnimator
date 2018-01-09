package com.johnnym.recyclerviewdemo.recyclerviewfull.domain

import com.johnnym.recyclerviewdemo.common.domain.UseCase
import com.johnnym.recyclerviewdemo.recyclerviewfull.data.TaxiListRepository
import io.reactivex.Scheduler
import io.reactivex.Single

class GetTaxiList(
        private val taxiListRepository: TaxiListRepository,
        subscribeOnScheduler: Scheduler,
        observeOnScheduler: Scheduler
) : UseCase<TaxiList, GetTaxiList.Params>(subscribeOnScheduler, observeOnScheduler) {

    override fun buildUseCase(params: Params): Single<TaxiList> {
        var taxiListSingle = taxiListRepository.getTaxiListSingle()

        taxiListSingle = when (params.taxiStatusFilter) {
            TaxiStatusFilter.ONLY_AVAILABLE -> taxiListSingle.map {
                TaxiList(it.taxiListItems
                        .filter {
                            it.taxiStatus == TaxiStatus.AVAILABLE
                        })
            }
            TaxiStatusFilter.NO_FILTER -> taxiListSingle
        }

        return taxiListSingle.map {
            TaxiList(it.taxiListItems
                    .sortedWith(params.taxiSortOption.createComparator()))
        }
    }

    class Params(
            val taxiStatusFilter: TaxiStatusFilter,
            val taxiSortOption: TaxiSortOption)
}
