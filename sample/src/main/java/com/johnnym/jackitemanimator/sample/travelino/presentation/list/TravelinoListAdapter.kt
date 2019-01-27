package com.johnnym.jackitemanimator.sample.travelino.presentation.list

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.johnnym.jackitemanimator.sample.taxilist.presentation.taxilist.Change
import com.johnnym.jackitemanimator.sample.taxilist.presentation.taxilist.createCombinedPayload
import com.johnnym.jackitemanimator.sample.travelino.presentation.TravelinoListItemViewModel

class TravelinoListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items = mutableListOf<TravelinoListItemViewModel>()

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int {
        val viewType = when (items[position].style) {
            TravelinoListItemViewModel.Style.FULL_WIDTH -> ViewType.NORMAL
            TravelinoListItemViewModel.Style.HALF_WIDTH -> ViewType.SQUARE
        }

        return viewType.ordinal
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val context = parent.context

        return when (ViewType.from(viewType)) {
            ViewType.NORMAL -> TravelinoNormalItemViewHolder(context, TravelinoNormalItemView(context))
            ViewType.SQUARE -> TravelinoSquareItemViewHolder(context, TravelinoSquareItemView(context))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TravelinoNormalItemViewHolder -> holder.bind(items[position])
            is TravelinoSquareItemViewHolder -> holder.bind(items[position])
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: List<Any>) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        } else {
            val combinedChange = createCombinedPayload(payloads as List<Change<TravelinoListItemViewModel>>)
            val oldData = combinedChange.oldData
            val newData = combinedChange.newData

            if (newData.price != oldData.price) {
                when (holder) {
                    is TravelinoNormalItemViewHolder -> holder.view.price.text = newData.price
                    is TravelinoSquareItemViewHolder -> holder.view.price.text = newData.price
                }
            }

            if (newData.discountPercentage != oldData.discountPercentage) {
                when (holder) {
                    is TravelinoNormalItemViewHolder -> holder.view.discountPercentage.text = newData.discountPercentage
                    is TravelinoSquareItemViewHolder -> holder.view.discountPercentage.text = newData.discountPercentage
                }
            }

            if (newData.infoMessage != oldData.infoMessage) {
                when (holder) {
                    is TravelinoNormalItemViewHolder -> holder.view.infoMessage.text = newData.infoMessage
                    is TravelinoSquareItemViewHolder -> holder.view.infoMessage.text = newData.infoMessage
                }
            }
        }
    }

    fun setItems(newItems: List<TravelinoListItemViewModel>) {
        val result = DiffUtil.calculateDiff(TravelinoListDiffUtilCallback(this.items, newItems))
        result.dispatchUpdatesTo(this)
        this.items.clear()
        this.items.addAll(newItems)
    }

    fun getItem(position: Int) = items[position]

    enum class ViewType {
        NORMAL,
        SQUARE;

        companion object {
            fun from(viewType: Int) = ViewType.values()[viewType]
        }
    }

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

        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
            val oldItem = oldItems[oldItemPosition]
            val newItem = newItems[newItemPosition]

            return Change(
                    oldItem,
                    newItem)
        }
    }
}
