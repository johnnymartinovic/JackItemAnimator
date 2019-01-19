package com.johnnym.jackitemanimator.sample.travelino.presentation.list

import androidx.recyclerview.widget.DiffUtil
import com.johnnym.jackitemanimator.sample.travelino.presentation.TravelinoListItemViewModel

class TravelinoListDiffUtilCallback(
        private var oldItems: List<TravelinoListItemViewModel>,
        private var newItems: List<TravelinoListItemViewModel>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldItems.size

    override fun getNewListSize(): Int = newItems.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldItems[oldItemPosition].id == newItems[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldItems[oldItemPosition] == newItems[newItemPosition]
}
