package com.johnnym.jackitemanimator.sample.travelino.presentation

import com.johnnym.jackitemanimator.sample.travelino.domain.TravelinoList
import com.johnnym.jackitemanimator.sample.travelino.domain.TravelinoItem
import kotlin.math.roundToInt

class TravelinoListViewModelMapper {

    companion object {
        private const val PRICE_PREFIX = "$"
        private const val ORIGINAL_PRICE_PREFIX = "Was $"
    }

    fun map(travelinoList: TravelinoList): TravelinoListViewModel {
        val listItemViewModels = travelinoList.listItems.map(::map)

        return TravelinoListViewModel(listItemViewModels)
    }

    private fun map(item: TravelinoItem): TravelinoItemViewModel {
        val style = when (item.style) {
            TravelinoItem.Style.FULL_WIDTH -> TravelinoItemViewModel.Style.FULL_WIDTH
            TravelinoItem.Style.HALF_WIDTH -> TravelinoItemViewModel.Style.HALF_WIDTH
        }

        return TravelinoItemViewModel(
                item.id,
                item.name,
                "$PRICE_PREFIX%d".format(item.price),
                "$ORIGINAL_PRICE_PREFIX%d".format(item.originalPrice),
                "${item.discountPercentage.roundToInt()}%",
                item.imageUrl,
                item.infoMessage,
                style)
    }
}