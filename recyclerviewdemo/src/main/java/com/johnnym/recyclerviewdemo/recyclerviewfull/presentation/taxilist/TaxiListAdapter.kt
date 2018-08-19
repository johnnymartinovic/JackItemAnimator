package com.johnnym.recyclerviewdemo.recyclerviewfull.presentation.taxilist

import android.content.Context
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.johnnym.recyclerviewdemo.recyclerviewfull.presentation.TaxiListItemViewModel

class TaxiListAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var viewType = ViewType.NORMAL

    private var items = listOf<TaxiListItemViewModel>()

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int = viewType.ordinal

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (ViewType.from(viewType)) {
            ViewType.NORMAL -> NormalItemViewHolder(context, NormalTaxiItemView(context))
            ViewType.SQUARE -> SquareItemViewHolder(context, SquareTaxiItemView(context))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is NormalItemViewHolder -> holder.bind(items[position])
            is SquareItemViewHolder -> holder.bind(items[position])
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: List<Any>) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        } else {
            // It isn't exactly necessary to changes that will be animated with
            // TaxiListItemAnimator, but it is done because we don't want to depend on whether the
            // correct ItemAnimator is selected or not
            val taxiListItemPayload = payloads.last() as TaxiListItemPayload

            val taxiStatusChange = taxiListItemPayload.taxiStatusChange
            val distanceChange = taxiListItemPayload.distanceChange

            if (taxiStatusChange != null) {
                when (holder) {
                    is NormalItemViewHolder -> holder.setTaxiStatus(taxiStatusChange.new)
                    is SquareItemViewHolder -> holder.setTaxiStatus(taxiStatusChange.new)
                }
            }
            if (distanceChange != null) {
                when (holder) {
                    is NormalItemViewHolder -> holder.setDistanceValue(distanceChange.new)
                }
            }
        }
    }

    fun setItems(newItems: List<TaxiListItemViewModel>) {
        val oldItems = this.items
        val result = DiffUtil.calculateDiff(DiffCallback(oldItems, newItems))
        this.items = newItems
        result.dispatchUpdatesTo(this)
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
