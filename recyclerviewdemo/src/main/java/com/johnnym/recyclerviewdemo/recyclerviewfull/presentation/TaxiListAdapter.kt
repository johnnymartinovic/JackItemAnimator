package com.johnnym.recyclerviewdemo.recyclerviewfull.presentation

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.support.annotation.ColorInt
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
import android.view.ViewAnimationUtils
import android.os.Build
import butterknife.BindColor
import butterknife.BindString


class TaxiListAdapter(private val context: Context) : RecyclerView.Adapter<TaxiListAdapter.ItemViewHolder>() {

    companion object {

        const val CHANGE_ANIMATION_DURATION = 1000L
    }

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

        @BindView(R.id.reveal_helper_view) lateinit var revealHelperView: View
        @BindView(R.id.status_bar) lateinit var statusBar: View
        @BindView(R.id.driver_photo) lateinit var driverPhoto: ImageView
        @BindView(R.id.driver_name) lateinit var driverName: TextView
        @BindView(R.id.stars) lateinit var stars: TextView
        @BindView(R.id.distance) lateinit var distance: TextView

        @BindString(R.string.taxi_list_item_stars_format) lateinit var starsFormattedText: String
        @BindString(R.string.taxi_list_item_distance_format) lateinit var distanceFormattedText: String

        @JvmField @ColorInt @BindColor(R.color.taxi_list_item_status_available) var statusAvailableColor: Int = 0
        @JvmField @ColorInt @BindColor(R.color.taxi_list_item_status_unavailable) var statusUnavailableColor: Int = 0
        @JvmField @ColorInt @BindColor(R.color.taxi_list_item_distance_decreased_signal) var distanceDecreasedSignalColor: Int = 0
        @JvmField @ColorInt @BindColor(R.color.taxi_list_item_distance_increased_signal) var distanceIncreasedSignalColor: Int = 0
        @JvmField @ColorInt @BindColor(android.R.color.transparent) var transparentColor: Int = 0

        init {
            ButterKnife.bind(this, itemView)
        }

        fun bind(item: TaxiListItemPresentable) {
            Glide.with(context)
                    .load(item.driverPhotoUrl)
                    .apply(RequestOptions()
                            .centerCrop()
                            .placeholder(R.drawable.ic_star)
                            .error(R.drawable.ic_distance)
                            .dontAnimate())
                    .into(driverPhoto)

            when (item.taxiStatus) {
                TaxiStatus.AVAILABLE -> statusBar.setBackgroundColor(statusAvailableColor)
                TaxiStatus.OCCUPIED -> statusBar.setBackgroundColor(statusUnavailableColor)
            }

            driverName.text = item.driverName
            stars.text = String.format(starsFormattedText, item.stars)
            distance.text = String.format(distanceFormattedText, item.distance)
        }

        fun animateTaxiStatusColorChange(taxiStatusChange: Change<TaxiStatus>) {
            @ColorInt val startColor: Int
            @ColorInt val endColor: Int
            if (taxiStatusChange.old == TaxiStatus.AVAILABLE) {
                startColor = statusAvailableColor
                endColor = statusUnavailableColor
            } else {
                startColor = statusUnavailableColor
                endColor = statusAvailableColor
            }
            ValueAnimator().apply {
                setIntValues(startColor, endColor)
                setEvaluator(ArgbEvaluator())
                addUpdateListener { statusBar.setBackgroundColor(it.animatedValue as Int) }
                duration = CHANGE_ANIMATION_DURATION
                start()
            }
        }

        fun animateDistanceChange(distanceChange: Change<Float>) {
            ValueAnimator().apply {
                setFloatValues(distanceChange.old, distanceChange.new)
                addUpdateListener { distance.text = String.format(distanceFormattedText, it.animatedValue as Float) }
                duration = CHANGE_ANIMATION_DURATION
                start()
            }

            val x = (distance.x + distance.width / 2).toInt()
            val y = (distance.y + distance.height / 2).toInt()

            val startRadius = 0
            val endRadius = Math.hypot(distance.width.toDouble(), distance.height.toDouble()).toInt()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val anim = ViewAnimationUtils.createCircularReveal(revealHelperView, x, y, startRadius.toFloat(), endRadius.toFloat())
                        .apply {
                            duration = CHANGE_ANIMATION_DURATION
                        }
                anim.start()

                @ColorInt val normalColor = transparentColor
                @ColorInt val effectColor =
                        if (distanceChange.old > distanceChange.new) distanceDecreasedSignalColor
                        else distanceIncreasedSignalColor

                ValueAnimator().apply {
                    setIntValues(normalColor, effectColor, normalColor)
                    setEvaluator(ArgbEvaluator())
                    addUpdateListener { revealHelperView.setBackgroundColor(it.animatedValue as Int) }
                    duration = CHANGE_ANIMATION_DURATION
                    start()
                }
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
