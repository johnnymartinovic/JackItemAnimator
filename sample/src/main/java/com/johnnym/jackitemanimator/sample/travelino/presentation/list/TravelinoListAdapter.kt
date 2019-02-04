package com.johnnym.jackitemanimator.sample.travelino.presentation.list

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.johnnym.jackitemanimator.sample.R
import com.johnnym.jackitemanimator.sample.travelino.presentation.TravelinoItemViewModel

class TravelinoListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items = mutableListOf<TravelinoItemViewModel>()

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int {
        val viewType = when (items[position].style) {
            TravelinoItemViewModel.Style.FULL_WIDTH -> ViewType.NORMAL
            TravelinoItemViewModel.Style.HALF_WIDTH -> ViewType.SQUARE
        }

        return viewType.ordinal
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val context = parent.context

        return when (ViewType.from(viewType)) {
            ViewType.NORMAL -> NormalTravelinoItemViewHolder(context, NormalTravelinoItemView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        context.resources.getDimensionPixelSize(R.dimen.travelino_item_height_normal))
            })
            ViewType.SQUARE -> SquareTravelinoItemViewHolder(context, SquareTravelinoItemView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        context.resources.getDimensionPixelSize(R.dimen.travelino_item_height_square))
            })
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is NormalTravelinoItemViewHolder -> holder.bind(items[position])
            is SquareTravelinoItemViewHolder -> holder.bind(items[position])
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: List<Any>) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        } else {
            val combinedChange = createCombinedPayload(payloads as List<Change<TravelinoItemViewModel>>)
            val oldData = combinedChange.oldData
            val newData = combinedChange.newData

            if (newData.price != oldData.price) {
                when (holder) {
                    is NormalTravelinoItemViewHolder -> holder.view.price.text = newData.price
                    is SquareTravelinoItemViewHolder -> holder.view.price.text = newData.price
                }
            }

            if (newData.discountPercentage != oldData.discountPercentage) {
                when (holder) {
                    is NormalTravelinoItemViewHolder -> holder.view.discountPercentage.text = newData.discountPercentage
                    is SquareTravelinoItemViewHolder -> holder.view.discountPercentage.text = newData.discountPercentage
                }
            }

            if (newData.infoMessage != oldData.infoMessage) {
                when (holder) {
                    is NormalTravelinoItemViewHolder -> holder.view.infoMessage.text = newData.infoMessage
                    is SquareTravelinoItemViewHolder -> holder.view.infoMessage.text = newData.infoMessage
                }
            }
        }
    }

    fun setItems(newItems: List<TravelinoItemViewModel>) {
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

data class Change<out T>(
        val oldData: T,
        val newData: T)

fun <T> createCombinedPayload(payloads: List<Change<T>>): Change<T> {
    assert(payloads.isNotEmpty())
    val firstChange = payloads.first()
    val lastChange = payloads.last()

    return Change(firstChange.oldData, lastChange.newData)
}
