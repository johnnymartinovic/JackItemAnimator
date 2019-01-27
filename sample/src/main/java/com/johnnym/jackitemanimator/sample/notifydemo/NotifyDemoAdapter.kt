package com.johnnym.jackitemanimator.sample.notifydemo

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.johnnym.jackitemanimator.sample.travelino.presentation.TravelinoItemViewModel
import com.johnnym.jackitemanimator.sample.travelino.presentation.list.NormalTravelinoItemView
import com.johnnym.jackitemanimator.sample.travelino.presentation.list.NormalTravelinoItemViewHolder

class NotifyDemoAdapter : RecyclerView.Adapter<NormalTravelinoItemViewHolder>() {

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
        if (payloads.isEmpty())
            super.onBindViewHolder(holder, position, payloads)
        else {
            if (payloads.any { it is InfoMessageChanged })
                holder.view.infoMessage.text = this.items[position].infoMessage
        }
    }

    fun setItems(newItems: List<TravelinoItemViewModel>) {
        this.items.clear()
        this.items.addAll(newItems)
        notifyDataSetChanged()
    }

    fun insertItem(viewModel: TravelinoItemViewModel, position: Int) {
        this.items.add(position, viewModel)
        notifyItemInserted(position)
    }

    fun moveItem(fromPosition: Int, toPosition: Int) {
        if (fromPosition == toPosition) return

        val movingItem = this.items.removeAt(fromPosition)
        if (fromPosition < toPosition) {
            this.items.add(toPosition - 1, movingItem)
        } else {
            this.items.add(toPosition, movingItem)
        }

        notifyItemMoved(fromPosition, toPosition)
    }

    fun removeItemRange(positionStart: Int, itemCount: Int) {
        repeat(itemCount) {
            this.items.removeAt(positionStart)
        }
        notifyItemRangeRemoved(positionStart, itemCount)
    }

    fun changeItem(position: Int, newInfoMessage: String) {
        val oldItem = this.items[position]
        val newItem = oldItem.copy(
                infoMessage = newInfoMessage
        )
        this.items[position] = newItem
        notifyItemChanged(position)
    }

    fun changeItemWithPayload(position: Int, newInfoMessage: String) {
        val oldItem = this.items[position]
        val newItem = oldItem.copy(
                infoMessage = newInfoMessage
        )
        this.items[position] = newItem
        notifyItemChanged(position, InfoMessageChanged())
    }

    class InfoMessageChanged
}
