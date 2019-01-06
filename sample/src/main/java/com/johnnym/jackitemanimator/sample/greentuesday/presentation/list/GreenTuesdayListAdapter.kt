package com.johnnym.jackitemanimator.sample.greentuesday.presentation.list

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.johnnym.jackitemanimator.sample.greentuesday.presentation.GreenTuesdayListItemViewModel

class GreenTuesdayListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var viewType = ViewType.NORMAL

    private var items = mutableListOf<GreenTuesdayListItemViewModel>()

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int = viewType.ordinal

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val context = parent.context
        return when (ViewType.from(viewType)) {
            ViewType.NORMAL -> GreenTuesdayNormalItemViewHolder(context, GreenTuesdayNormalItemView(context))
            ViewType.SQUARE -> GreenTuesdaySquareItemViewHolder(context, GreenTuesdaySquareItemView(context))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is GreenTuesdayNormalItemViewHolder -> holder.bind(items[position])
            is GreenTuesdaySquareItemViewHolder -> holder.bind(items[position])
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: List<Any>) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        } else {
            // TODO
        }
    }

    fun setItems(newItems: List<GreenTuesdayListItemViewModel>) {
        this.items.clear()
        this.items.addAll(newItems)
        notifyDataSetChanged()
    }

    fun setViewType(viewType: ViewType) {
        this.viewType = viewType
        notifyItemRangeChanged(0, itemCount)
    }

    enum class ViewType {
        NORMAL,
        SQUARE;

        companion object {
            fun from(viewType: Int) = ViewType.values()[viewType]
        }
    }
}
