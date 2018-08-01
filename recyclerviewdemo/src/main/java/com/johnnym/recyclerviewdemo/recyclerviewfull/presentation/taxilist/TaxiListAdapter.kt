package com.johnnym.recyclerviewdemo.recyclerviewfull.presentation.taxilist

import android.content.Context
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.github.rstanic12.resourceful.bindColor
import com.github.rstanic12.resourceful.bindString
import com.johnnym.recyclerviewdemo.R
import com.johnnym.recyclerviewdemo.recyclerviewfull.domain.TaxiStatus
import com.johnnym.recyclerviewdemo.common.binding.bindView
import com.johnnym.recyclerviewdemo.recyclerviewfull.presentation.TaxiListItemViewModel

class TaxiListAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {

        // TODO make this enum
        const val NORMAL_VIEW_TYPE = 0
        const val SQUARE_VIEW_TYPE = 1
    }

    private var viewType = NORMAL_VIEW_TYPE

    private var items = listOf<TaxiListItemViewModel>()

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int = viewType

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            NORMAL_VIEW_TYPE -> ItemViewHolder(
                    context,
                    inflater.inflate(R.layout.taxi_list_item, parent, false))
            SQUARE_VIEW_TYPE -> SquareItemViewHolder(
                    context,
                    inflater.inflate(R.layout.taxi_list_item_grid, parent, false))
            else -> throw IllegalStateException("Undefined view type.")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ItemViewHolder -> holder.bind(items[position])
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
                    is ItemViewHolder -> holder.setTaxiStatus(taxiStatusChange.new)
                    is SquareItemViewHolder -> holder.setTaxiStatus(taxiStatusChange.new)
                }
            }
            if (distanceChange != null) {
                when (holder) {
                    is ItemViewHolder -> holder.setDistanceValue(distanceChange.new)
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

    fun setViewType(viewType: Int) {
        this.viewType = viewType
        notifyItemRangeChanged(0, itemCount)
    }

    class ItemViewHolder(
            private val context: Context,
            itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        val backgroundHelperView: View by bindView(R.id.background_helper_view)
        val revealHelperView: View by bindView(R.id.reveal_helper_view)
        val statusBar: View by bindView(R.id.status_bar)
        val driverPhoto: ImageView by bindView(R.id.driver_photo)
        val driverName: TextView by bindView(R.id.driver_name)
        val starIcon: ImageView by bindView(R.id.star_icon)
        val stars: TextView by bindView(R.id.stars)
        val distanceIcon: ImageView by bindView(R.id.distance_icon)
        val distance: TextView by bindView(R.id.distance)

        val starsFormattedText: String by bindString(R.string.taxi_list_item_stars_format)
        val distanceFormattedText: String by bindString(R.string.taxi_list_item_distance_format)

        val statusAvailableColor: Int by bindColor(R.color.taxi_list_item_status_available)
        val statusUnavailableColor: Int by bindColor(R.color.taxi_list_item_status_unavailable)
        val distanceDecreasedSignalColor: Int by bindColor(R.color.taxi_list_item_distance_decreased_signal)
        val distanceIncreasedSignalColor: Int by bindColor(R.color.taxi_list_item_distance_increased_signal)
        val transparentColor: Int by bindColor(android.R.color.transparent)

        fun bind(item: TaxiListItemViewModel) {
            Glide.with(context)
                    .load(item.driverPhotoUrl)
                    .apply(RequestOptions()
                            .placeholder(R.drawable.ic_star)
                            .error(R.drawable.ic_distance)
                            .dontAnimate())
                    .into(driverPhoto)

            driverName.text = item.driverName
            stars.text = String.format(starsFormattedText, item.stars)
            setTaxiStatus(item.taxiStatus)
            setDistanceValue(item.distance)
        }

        fun setTaxiStatus(taxiStatus: TaxiStatus) {
            when (taxiStatus) {
                TaxiStatus.AVAILABLE -> statusBar.setBackgroundColor(statusAvailableColor)
                TaxiStatus.OCCUPIED -> statusBar.setBackgroundColor(statusUnavailableColor)
            }
        }

        fun setDistanceValue(distance: Float) {
            this.distance.text = String.format(distanceFormattedText, distance)
        }
    }

    class SquareItemViewHolder(
            private val context: Context,
            itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        val statusBar: View by bindView(R.id.status_bar)
        val driverPhoto: ImageView by bindView(R.id.driver_photo)

        val statusAvailableColor: Int by bindColor(R.color.taxi_list_item_status_available)
        val statusUnavailableColor: Int by bindColor(R.color.taxi_list_item_status_unavailable)

        fun bind(item: TaxiListItemViewModel) {
            Glide.with(context)
                    .load(item.driverPhotoUrl)
                    .apply(RequestOptions()
                            .placeholder(R.drawable.ic_star)
                            .error(R.drawable.ic_distance)
                            .dontAnimate())
                    .into(driverPhoto)

            setTaxiStatus(item.taxiStatus)
        }

        fun setTaxiStatus(taxiStatus: TaxiStatus) {
            when (taxiStatus) {
                TaxiStatus.AVAILABLE -> statusBar.setBackgroundColor(statusAvailableColor)
                TaxiStatus.OCCUPIED -> statusBar.setBackgroundColor(statusUnavailableColor)
            }
        }
    }
}
