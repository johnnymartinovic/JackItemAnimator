package com.johnnym.jackitemanimator.sample.diffutildemo

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.johnnym.jackitemanimator.sample.taxilist.presentation.taxilist.Change
import com.johnnym.jackitemanimator.sample.taxilist.presentation.taxilist.createCombinedPayload
import com.johnnym.jackitemanimator.sample.travelino.presentation.TravelinoItemViewModel
import com.johnnym.jackitemanimator.sample.travelino.presentation.list.NormalTravelinoItemView
import com.johnnym.jackitemanimator.sample.travelino.presentation.list.NormalTravelinoItemViewHolder

class DiffUtilDemoAdapter : RecyclerView.Adapter<NormalTravelinoItemViewHolder>() {

    private var items = mutableListOf<TravelinoItemViewModel>()

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NormalTravelinoItemViewHolder {
        val context = parent.context
        return NormalTravelinoItemViewHolder(context, NormalTravelinoItemView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)
        })
    }

    override fun onBindViewHolder(holder: NormalTravelinoItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun onBindViewHolder(holder: NormalTravelinoItemViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            val combinedChange = createCombinedPayload(payloads as List<Change<TravelinoItemViewModel>>)
            val oldData = combinedChange.oldData
            val newData = combinedChange.newData

            if (newData.price != oldData.price) {
                holder.view.price.text = newData.price
            }

            if (newData.discountPercentage != oldData.discountPercentage) {
                holder.view.discountPercentage.text = newData.discountPercentage
            }

            if (newData.infoMessage != oldData.infoMessage) {
                holder.view.infoMessage.text = newData.infoMessage
            }
        }
    }

    fun setItems(newItems: List<TravelinoItemViewModel>) {
        val result = DiffUtil.calculateDiff(TravelinoListDiffUtilCallback(this.items, newItems))
        result.dispatchUpdatesTo(this)
        this.items.clear()
        this.items.addAll(newItems)
    }

    class TravelinoListDiffUtilCallback(
            private var oldItems: List<TravelinoItemViewModel>,
            private var newItems: List<TravelinoItemViewModel>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldItems.size

        override fun getNewListSize(): Int = newItems.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                oldItems[oldItemPosition].id == newItems[newItemPosition].id

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                oldItems[oldItemPosition] == newItems[newItemPosition]

        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
            val oldItem = oldItems[oldItemPosition]
            val newItem = newItems[newItemPosition]

            return Change(
                    oldItem,
                    newItem)
        }
    }
}
