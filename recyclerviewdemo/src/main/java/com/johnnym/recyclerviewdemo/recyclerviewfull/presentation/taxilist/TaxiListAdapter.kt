package com.johnnym.recyclerviewdemo.recyclerviewfull.presentation.taxilist

import android.content.Context
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import com.johnnym.recyclerviewdemo.recyclerviewfull.presentation.TaxiListItemViewModel

class TaxiListAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var viewType = ViewType.NORMAL

    private var items = mutableListOf<TaxiListItemViewModel>()

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
            val taxiListItemPayload = payloads.last() as Change<TaxiListItemState>

            val oldTaxiStatus = taxiListItemPayload.oldData.taxiStatus
            val newTaxiStatus = taxiListItemPayload.newData.taxiStatus
            val oldDistance = taxiListItemPayload.oldData.distance
            val newDistance = taxiListItemPayload.newData.distance

            if (oldTaxiStatus != newTaxiStatus) {
                when (holder) {
                    is NormalItemViewHolder -> holder.setTaxiStatus(newTaxiStatus)
                    is SquareItemViewHolder -> holder.setTaxiStatus(newTaxiStatus)
                }
            }
            if (oldDistance != newDistance) {
                when (holder) {
                    is NormalItemViewHolder -> holder.setDistanceValue(newDistance)
                }
            }
        }
    }

    fun setItems(newItems: List<TaxiListItemViewModel>) {
        val result = DiffUtil.calculateDiff(DiffCallback(this.items, newItems))
        result.dispatchUpdatesTo(this)
        this.items.clear()
        this.items.addAll(newItems)
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
