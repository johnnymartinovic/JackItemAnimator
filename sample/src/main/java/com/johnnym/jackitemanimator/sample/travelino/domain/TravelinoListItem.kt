package com.johnnym.jackitemanimator.sample.travelino.domain

data class TravelinoListItem(
        val id: String,
        val name: String,
        val price: Int,
        val originalPrice: Int,
        val imageUrl: String,
        val infoMessage: String?) {

    val discountPercentage: Float = ((price.toFloat() / originalPrice) - 1) * 100
}
