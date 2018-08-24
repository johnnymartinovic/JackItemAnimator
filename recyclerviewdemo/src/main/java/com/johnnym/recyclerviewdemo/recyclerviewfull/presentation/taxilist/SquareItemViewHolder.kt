package com.johnnym.recyclerviewdemo.recyclerviewfull.presentation.taxilist

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.github.rstanic12.resourceful.bindColor
import com.johnnym.recyclerviewdemo.R
import com.johnnym.recyclerviewdemo.recyclerviewfull.domain.TaxiStatus
import com.johnnym.recyclerviewdemo.recyclerviewfull.presentation.TaxiListItemViewModel

class SquareItemViewHolder(
        private val context: Context,
        val squareItemView: SquareTaxiItemView
) : RecyclerView.ViewHolder(squareItemView) {

    val statusAvailableColor: Int by bindColor(R.color.taxi_list_item_status_available)
    val statusUnavailableColor: Int by bindColor(R.color.taxi_list_item_status_unavailable)

    fun bind(item: TaxiListItemViewModel) {
        Glide.with(context)
                .load(item.driverPhotoUrl)
                .apply(RequestOptions()
                        .placeholder(R.drawable.ic_star)
                        .error(R.drawable.ic_distance)
                        .dontAnimate())
                .into(squareItemView.driverPhoto)

        setTaxiStatus(item.taxiStatus)
    }

    fun setTaxiStatus(taxiStatus: TaxiStatus) {
        when (taxiStatus) {
            TaxiStatus.AVAILABLE -> squareItemView.statusBar.setBackgroundColor(statusAvailableColor)
            TaxiStatus.OCCUPIED -> squareItemView.statusBar.setBackgroundColor(statusUnavailableColor)
        }
    }
}