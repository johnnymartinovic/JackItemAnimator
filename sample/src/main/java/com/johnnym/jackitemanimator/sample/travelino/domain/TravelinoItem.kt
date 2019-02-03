package com.johnnym.jackitemanimator.sample.travelino.domain

data class TravelinoItem(
        val id: String,
        val name: String,
        val price: Int,
        val originalPrice: Int,
        val imageUrl: String,
        val infoMessage: String,
        val style: Style = Style.HALF_WIDTH) {

    val discountPercentage: Float = ((price.toFloat() / originalPrice) - 1) * 100

    enum class Style {
        FULL_WIDTH,
        HALF_WIDTH
    }
}
