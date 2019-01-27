package com.johnnym.jackitemanimator.sample.travelino.presentation.list

import android.content.Context
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.johnnym.jackitemanimator.sample.travelino.presentation.TravelinoListItemViewModel

class TravelinoSquareItemViewHolder(
        private val context: Context,
        val view: TravelinoSquareItemView
) : RecyclerView.ViewHolder(view) {

    fun bind(viewModel: TravelinoListItemViewModel) {
        view.title.text = viewModel.name
        view.price.text = viewModel.price
        view.originalPrice.text = viewModel.originalPrice
        view.discountPercentage.text = viewModel.discountPercentage

        Glide.with(context)
                .load(viewModel.imageUrl)
                .apply(RequestOptions()
                        .placeholder(android.R.color.white)
                        .error(android.R.color.black)
                        .dontAnimate())
                .into(view.image)

        viewModel.infoMessage
                ?.let { view.infoMessage.text = it }
                ?: let { view.infoMessage.isGone = true }
    }
}