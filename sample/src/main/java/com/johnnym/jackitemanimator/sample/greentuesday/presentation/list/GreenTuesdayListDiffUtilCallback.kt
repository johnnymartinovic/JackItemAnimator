package com.johnnym.jackitemanimator.sample.greentuesday.presentation.list

import androidx.recyclerview.widget.DiffUtil
import com.johnnym.jackitemanimator.sample.greentuesday.presentation.GreenTuesdayListItemViewModel

class GreenTuesdayListDiffUtilCallback(
        private var oldItems: List<GreenTuesdayListItemViewModel>,
        private var newItems: List<GreenTuesdayListItemViewModel>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldItems.size

    override fun getNewListSize(): Int = newItems.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldItems[oldItemPosition].id == newItems[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldItems[oldItemPosition] == newItems[newItemPosition]
}
