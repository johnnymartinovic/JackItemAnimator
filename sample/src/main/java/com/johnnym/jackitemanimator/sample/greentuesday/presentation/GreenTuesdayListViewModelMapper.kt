package com.johnnym.jackitemanimator.sample.greentuesday.presentation

import com.johnnym.jackitemanimator.sample.greentuesday.domain.GreenTuesdayList
import kotlin.math.roundToInt

class GreenTuesdayListViewModelMapper {

    companion object {
        private const val PRICE_PREFIX = "$"
    }

    fun map(greenTuesdayList: GreenTuesdayList): GreenTuesdayListViewModel {
        val listItemViewModels = greenTuesdayList.listItems.map {
            GreenTuesdayListItemViewModel(
                    it.id,
                    it.name,
                    "$PRICE_PREFIX%.2f".format(it.price),
                    "$PRICE_PREFIX%.2f".format(it.originalPrice),
                    "${it.discountPercentage.roundToInt()}%",
                    it.imageUrl,
                    it.infoMessage)
        }

        return GreenTuesdayListViewModel(listItemViewModels)
    }
}