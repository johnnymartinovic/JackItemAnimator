package com.johnnym.jackitemanimator.sample.taxilist.presentation.taxilist

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.github.rstanic12.resourceful.bindColor
import com.johnnym.jackitemanimator.sample.R
import com.johnnym.jackitemanimator.sample.taxilist.domain.TaxiStatus
import com.johnnym.jackitemanimator.sample.taxilist.presentation.TaxiListItemViewModel

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
                        .placeholder(android.R.color.white)
                        .error(android.R.color.black)
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