package com.johnnym.jackitemanimator.sample.travelino.presentation.list

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.johnnym.jackitemanimator.sample.travelino.presentation.TravelinoItemViewModel

class NormalTravelinoItemViewHolder(
        private val context: Context,
        val view: NormalTravelinoItemView
) : RecyclerView.ViewHolder(view) {

    fun bind(viewModel: TravelinoItemViewModel) {
        view.title.text = viewModel.name
        view.price.text = viewModel.price
        view.originalPrice.text = viewModel.originalPrice
        view.discountPercentage.text = viewModel.discountPercentage
        view.infoMessage.text = viewModel.infoMessage

        Glide.with(context)
                .load(viewModel.imageUrl)
                .apply(RequestOptions()
                        .placeholder(android.R.color.white)
                        .error(android.R.color.black)
                        .dontAnimate())
                .into(view.image)
    }
}
