package com.johnnym.jackitemanimator.sample.greentuesday.presentation

import com.johnnym.jackitemanimator.sample.greentuesday.domain.GreenTuesdayList
import kotlin.math.roundToInt

class GreenTuesdayListViewModelMapper {

    companion object {
        private const val PRICE_PREFIX = "$"
    }

    fun map(greenTuesdayList: GreenTuesdayList): GreenTuesdayListViewModel {
        val listItemViewModels = greenTuesdayList.listItems.mapIndexed { index, item ->
            val style = if ((index + 1).rem(3) == 0) GreenTuesdayListItemViewModel.Style.FULL_WIDTH
            else GreenTuesdayListItemViewModel.Style.HALF_WIDTH

            GreenTuesdayListItemViewModel(
                    item.id,
                    item.name,
                    "$PRICE_PREFIX%.2f".format(item.price),
                    "$PRICE_PREFIX%.2f".format(item.originalPrice),
                    "${item.discountPercentage.roundToInt()}%",
                    item.imageUrl,
                    item.infoMessage,
                    style)
        }

        return GreenTuesdayListViewModel(listItemViewModels)
    }
}