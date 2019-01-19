package com.johnnym.jackitemanimator.sample.travelino.presentation.list

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
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
            // TODO
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
}
