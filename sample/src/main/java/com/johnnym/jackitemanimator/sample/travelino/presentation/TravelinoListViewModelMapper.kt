package com.johnnym.jackitemanimator.sample.travelino.presentation

import com.johnnym.jackitemanimator.sample.travelino.domain.TravelinoList
import kotlin.math.roundToInt

class TravelinoListViewModelMapper {

    companion object {
        private const val PRICE_PREFIX = "$"
    }

    fun map(travelinoList: TravelinoList): TravelinoListViewModel {
        val listItemViewModels = travelinoList.listItems.mapIndexed { index, item ->
            val style = if ((index + 1).rem(3) == 0) TravelinoListItemViewModel.Style.FULL_WIDTH
            else TravelinoListItemViewModel.Style.HALF_WIDTH

            TravelinoListItemViewModel(
                    item.id,
                    item.name,
                    "$PRICE_PREFIX%d".format(item.price),
                    "$PRICE_PREFIX%d".format(item.originalPrice),
                    "${item.discountPercentage.roundToInt()}%",
                    item.imageUrl,
                    item.infoMessage,
                    style)
        }

        return TravelinoListViewModel(listItemViewModels)
    }
}