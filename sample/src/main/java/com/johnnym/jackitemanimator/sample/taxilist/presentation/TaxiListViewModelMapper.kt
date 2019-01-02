package com.johnnym.jackitemanimator.sample.taxilist.presentation

import com.johnnym.jackitemanimator.sample.taxilist.domain.TaxiList

class TaxiListViewModelMapper {

    fun map(taxiList: TaxiList) : TaxiListViewModel {
        val taxiListItemViewModels: List<TaxiListItemViewModel> =
                taxiList.taxiListItems.map {
                    TaxiListItemViewModel(
                            it.taxiId,
                            it.taxiStatus,
                            it.driverPhotoUrl,
                            it.driverName,
                            it.stars,
                            it.distance)
                }

        return TaxiListViewModel(taxiListItemViewModels)
    }
}