package com.johnnym.recyclerviewdemo.recyclerviewfull.presentation

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.support.annotation.ColorInt
import android.support.v4.content.ContextCompat
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.johnnym.recyclerviewdemo.R
import com.johnnym.recyclerviewdemo.recyclerviewfull.domain.TaxiStatus

class TaxiListAdapter(private val context: Context) : RecyclerView.Adapter<TaxiListAdapter.ItemViewHolder>() {

    private var items = mutableListOf<TaxiListItemPresentable>()

    override fun getItemCount(): Int =
            items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ItemViewHolder(
                context,
                inflater.inflate(R.layout.taxi_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        } else {
            payloads.forEach {
                (it as ItemPayload).let {
                    if (it.taxiStatusChange != null)
                        holder.animateTaxiStatusColorChange(it.taxiStatusChange)
                    if (it.distanceChange != null)
                        holder.animateDistanceChange(it.distanceChange)
                }
            }
        }
    }

    fun setItems(items: List<TaxiListItemPresentable>) {
        DiffUtil.calculateDiff(DiffCallback(this.items, items)).dispatchUpdatesTo(this)

        this.items.clear()
        this.items.addAll(items)
    }

    class ItemViewHolder(
            private val context: Context,
            itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        @BindView(R.id.status_bar) lateinit var statusBar: View
        @BindView(R.id.driver_photo) lateinit var driverPhoto: ImageView
        @BindView(R.id.driver_name) lateinit var driverName: TextView
        @BindView(R.id.stars) lateinit var stars: TextView
        @BindView(R.id.distance) lateinit var distance: TextView

        init {
            ButterKnife.bind(this, itemView)
        }

        fun bind(item: TaxiListItemPresentable) {
            Glide.with(context)
                    .load(item.driverPhotoUrl)
                    .apply(RequestOptions()
                            .centerCrop()
                            .placeholder(R.drawable.ic_star)
                            .error(R.drawable.ic_distance))
                    .into(driverPhoto)

            when (item.taxiStatus) {
                TaxiStatus.AVAILABLE -> statusBar.setBackgroundColor(
                        ContextCompat.getColor(context, R.color.taxi_list_item_status_available))
                TaxiStatus.OCCUPIED -> statusBar.setBackgroundColor(
                        ContextCompat.getColor(context, R.color.taxi_list_item_status_unavailable))
            }

            driverName.text = item.driverName
            stars.text = context.getString(R.string.taxi_list_item_stars_format, item.stars)
            distance.text = context.getString(R.string.taxi_list_item_distance_format, item.distance)
        }

        fun animateTaxiStatusColorChange(taxiStatusChange: Change<TaxiStatus>) {
            @ColorInt val startColor: Int
            @ColorInt val endColor: Int
            if (taxiStatusChange.old == TaxiStatus.AVAILABLE) {
                startColor = ContextCompat.getColor(context, R.color.taxi_list_item_status_available)
                endColor = ContextCompat.getColor(context, R.color.taxi_list_item_status_unavailable)
            } else {
                startColor = ContextCompat.getColor(context, R.color.taxi_list_item_status_unavailable)
                endColor = ContextCompat.getColor(context, R.color.taxi_list_item_status_available)
            }
            ValueAnimator().apply {
                setIntValues(startColor, endColor)
                setEvaluator(ArgbEvaluator())
                addUpdateListener { statusBar.setBackgroundColor(it.animatedValue as Int) }
                duration = 1000
                start()
            }
        }

        fun animateDistanceChange(distanceChange: Change<Float>) {
            ValueAnimator().apply {
                setFloatValues(distanceChange.old, distanceChange.new)
                addUpdateListener { distance.text = context.getString(R.string.taxi_list_item_distance_format, it.animatedValue as Float) }
                duration = 1000
                start()
            }

            @ColorInt val normalColor = ContextCompat.getColor(context, R.color.taxi_list_item_distance_background)
            @ColorInt val effectColor =
                    if (distanceChange.old > distanceChange.new) ContextCompat.getColor(context, R.color.taxi_list_item_distance_decreased_signal)
                    else ContextCompat.getColor(context, R.color.taxi_list_item_distance_increased_signal)

            ValueAnimator().apply {
                setIntValues(normalColor, effectColor, normalColor)
                setEvaluator(ArgbEvaluator())
                addUpdateListener { distance.setBackgroundColor(it.animatedValue as Int) }
                duration = 1000
                start()
            }
        }
    }

    private class DiffCallback(
            var oldItems: List<TaxiListItemPresentable>,
            var newItems: List<TaxiListItemPresentable>
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

            return ItemPayload(taxiStatusChange, distanceChange)
        }
    }

    data class ItemPayload(
            val taxiStatusChange: Change<TaxiStatus>?,
            val distanceChange: Change<Float>?)

    data class Change<out T>(
            val old: T,
            val new: T)
}
