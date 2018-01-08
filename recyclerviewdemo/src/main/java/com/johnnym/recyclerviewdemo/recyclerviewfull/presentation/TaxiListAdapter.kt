package com.johnnym.recyclerviewdemo.recyclerviewfull.presentation

import android.content.Context
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

    fun setItems(items: List<TaxiListItemPresentable>) {
        val diffResult = DiffUtil.calculateDiff(DiffCallback(this.items, items))

        this.items.clear()
        this.items.addAll(items)

        diffResult.dispatchUpdatesTo(this)
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
    }
}
