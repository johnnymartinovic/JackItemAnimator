package com.johnnym.jackitemanimator.sample.taxilist.presentation.taxilist

import androidx.recyclerview.widget.DiffUtil
import com.johnnym.jackitemanimator.sample.taxilist.domain.TaxiStatus
import com.johnnym.jackitemanimator.sample.taxilist.presentation.TaxiListItemViewModel

class DiffCallback(
        private var oldItems: List<TaxiListItemViewModel>,
        private var newItems: List<TaxiListItemViewModel>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldItems.size

    override fun getNewListSize(): Int = newItems.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldItems[oldItemPosition].taxiId == newItems[newItemPosition].taxiId

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldItems[oldItemPosition] == newItems[newItemPosition]

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]

        return Change(
                TaxiListItemState(oldItem.taxiStatus, oldItem.distance),
                TaxiListItemState(newItem.taxiStatus, newItem.distance)
        )
    }
}

data class TaxiListItemState(
        val taxiStatus: TaxiStatus,
        val distance: Float
)

// TODO maybe put this in library? but be careful if other view types are available in the same list
data class Change<out T>(
        val oldData: T,
        val newData: T
)

fun <T> createCombinedPayload(payloads: List<Change<T>>): Change<T> {
    assert(payloads.isNotEmpty())
    val firstChange = payloads.first()
    val lastChange = payloads.last()

    return Change(firstChange.oldData, lastChange.newData)
}