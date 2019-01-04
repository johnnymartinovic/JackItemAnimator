package com.johnnym.jackitemanimator.sample.greentuesday.domain

data class GreenTuesdayListItem(
        val id: String,
        val name: String,
        val price: Float,
        val originalPrice: Float,
        val imageUrl: String,
        val infoMessage: String?) {

    val discountPercentage: Float = 1f - (price / originalPrice)
}
