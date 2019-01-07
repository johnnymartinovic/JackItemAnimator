package com.johnnym.jackitemanimator.sample.greentuesday.presentation

data class GreenTuesdayListItemViewModel(
        val id: String,
        val name: String,
        val price: String,
        val originalPrice: String,
        val discountPercentage: String,
        val imageUrl: String,
        val infoMessage: String?,
        val style: Style) {

    enum class Style {
        FULL_WIDTH,
        HALF_WIDTH
    }
}
