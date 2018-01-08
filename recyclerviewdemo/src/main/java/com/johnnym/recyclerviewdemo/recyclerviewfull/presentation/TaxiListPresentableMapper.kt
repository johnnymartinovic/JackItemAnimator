package com.johnnym.recyclerviewdemo.recyclerviewfull.presentation

import com.johnnym.recyclerviewdemo.recyclerviewfull.domain.TaxiList

class TaxiListPresentableMapper {

    fun map(taxiList: TaxiList) : TaxiListPresentable {
        val taxiListItemPresentables: List<TaxiListItemPresentable> =
                taxiList.taxiListItems.map {
                    TaxiListItemPresentable(
                            it.taxiId,
                            it.taxiStatus,
                            it.driverPhotoUrl,
                            it.driverName,
                            it.stars,
                            it.distance)
                }

        return TaxiListPresentable(taxiListItemPresentables)
    }
}