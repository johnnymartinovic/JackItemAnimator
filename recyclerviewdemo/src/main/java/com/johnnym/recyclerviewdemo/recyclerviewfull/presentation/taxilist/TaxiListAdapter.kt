package com.johnnym.recyclerviewdemo.recyclerviewfull.presentation.taxilist

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
import butterknife.BindColor
import butterknife.BindString
import com.johnnym.recyclerviewdemo.recyclerviewfull.presentation.TaxiListItemPresentable

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

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int, payloads: List<Any>) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        } else {
            val taxiListItemPayload = payloads.last() as TaxiListItemPayload
            val distanceChange = taxiListItemPayload.distanceChange
            if (distanceChange != null) {
                holder.setDistanceValue(distanceChange.new)
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

        @BindView(R.id.background_helper_view) lateinit var backgroundHelperView: View
        @BindView(R.id.reveal_helper_view) lateinit var revealHelperView: View
        @BindView(R.id.status_bar) lateinit var statusBar: View
        @BindView(R.id.driver_photo) lateinit var driverPhoto: ImageView
        @BindView(R.id.driver_name) lateinit var driverName: TextView
        @BindView(R.id.star_icon) lateinit var starIcon: ImageView
        @BindView(R.id.stars) lateinit var stars: TextView
        @BindView(R.id.distance_icon) lateinit var distanceIcon: ImageView
        @BindView(R.id.distance) lateinit var distance: TextView

        @BindString(R.string.taxi_list_item_stars_format) lateinit var starsFormattedText: String
        @BindString(R.string.taxi_list_item_distance_format) lateinit var distanceFormattedText: String

        @JvmField
        @ColorInt
        @BindColor(R.color.taxi_list_item_status_available)
        var statusAvailableColor: Int = 0
        @JvmField
        @ColorInt
        @BindColor(R.color.taxi_list_item_status_unavailable)
        var statusUnavailableColor: Int = 0
        @JvmField
        @ColorInt
        @BindColor(R.color.taxi_list_item_distance_decreased_signal)
        var distanceDecreasedSignalColor: Int = 0
        @JvmField
        @ColorInt
        @BindColor(R.color.taxi_list_item_distance_increased_signal)
        var distanceIncreasedSignalColor: Int = 0
        @JvmField
        @ColorInt
        @BindColor(android.R.color.transparent)
        var transparentColor: Int = 0

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
            setDistanceValue(item.distance)
        }

        fun setDistanceValue(distance: Float) {
            this.distance.text = String.format(distanceFormattedText, distance)
        }
    }
}
