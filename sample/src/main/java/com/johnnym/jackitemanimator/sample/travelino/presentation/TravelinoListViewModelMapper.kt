package com.johnnym.jackitemanimator.sample.travelino.presentation

import com.johnnym.jackitemanimator.sample.travelino.domain.TravelinoList
import com.johnnym.jackitemanimator.sample.travelino.domain.TravelinoListItem
import kotlin.math.roundToInt

class TravelinoListViewModelMapper {

    companion object {
        private const val PRICE_PREFIX = "$"
    }

    fun map(travelinoList: TravelinoList): TravelinoListViewModel {
        val listItemViewModels = travelinoList.listItems.map(::map)

        return TravelinoListViewModel(listItemViewModels)
    }

    fun map(item: TravelinoListItem): TravelinoListItemViewModel {
        val style = when (item.style) {
            TravelinoListItem.Style.FULL_WIDTH -> TravelinoListItemViewModel.Style.FULL_WIDTH
            TravelinoListItem.Style.HALF_WIDTH -> TravelinoListItemViewModel.Style.HALF_WIDTH
        }

        return TravelinoListItemViewModel(
                item.id,
                item.name,
                "$PRICE_PREFIX%d".format(item.price),
                "$PRICE_PREFIX%d".format(item.originalPrice),
                "${item.discountPercentage.roundToInt()}%",
                item.imageUrl,
                item.infoMessage,
                style)
    }
}