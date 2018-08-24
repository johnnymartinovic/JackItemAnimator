package com.johnnym.recyclerviewdemo.recyclerviewfull.presentation.taxilist

import androidx.recyclerview.widget.DiffUtil
import com.johnnym.recyclerviewdemo.recyclerviewfull.domain.TaxiStatus
import com.johnnym.recyclerviewdemo.recyclerviewfull.presentation.TaxiListItemViewModel

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

        val taxiStatusChange =
                if (oldItem.taxiStatus == newItem.taxiStatus) null
                else Change(oldItem.taxiStatus, newItem.taxiStatus)
        val distanceChange =
                if (oldItem.distance == newItem.distance) null
                else Change(oldItem.distance, newItem.distance)

        return TaxiListItemPayload(taxiStatusChange, distanceChange)
    }
}

fun createCombinedTaxiListItemPayload(payloads: List<Any>): TaxiListItemPayload {
    if (payloads.isEmpty()) return TaxiListItemPayload(null, null)
    if (payloads.size == 1) return payloads[0] as TaxiListItemPayload

    val firstTaxiStatusChangePayload = payloads.firstOrNull {
        (it as TaxiListItemPayload).taxiStatusChange != null
    } as TaxiListItemPayload?
    val firstTaxiStatusChange = firstTaxiStatusChangePayload?.taxiStatusChange

    val firstDistanceChangePayload = payloads.firstOrNull {
        (it as TaxiListItemPayload).distanceChange != null
    } as TaxiListItemPayload?
    val firstDistanceChange = firstDistanceChangePayload?.distanceChange

    val lastTaxiStatusChangePayload = payloads.lastOrNull {
        (it as TaxiListItemPayload).taxiStatusChange != null
    } as TaxiListItemPayload?
    val lastTaxiStatusChange = lastTaxiStatusChangePayload?.taxiStatusChange

    val lastDistanceChangePayload = payloads.lastOrNull {
        (it as TaxiListItemPayload).distanceChange != null
    } as TaxiListItemPayload?
    val lastDistanceChange = lastDistanceChangePayload?.distanceChange

    val combinedTaxiStatusChange = if (firstTaxiStatusChange != null && lastTaxiStatusChange != null) {
        Change(firstTaxiStatusChange.old, lastTaxiStatusChange.new)
    } else {
        null
    }

    val combinedDistanceChange = if (firstDistanceChange != null && lastDistanceChange != null) {
        Change(firstDistanceChange.old, lastDistanceChange.new)
    } else {
        null
    }

    return TaxiListItemPayload(
            combinedTaxiStatusChange,
            combinedDistanceChange)
}

data class TaxiListItemPayload(
        val taxiStatusChange: Change<TaxiStatus>?,
        val distanceChange: Change<Float>?)

data class Change<out T>(
        val old: T,
        val new: T)