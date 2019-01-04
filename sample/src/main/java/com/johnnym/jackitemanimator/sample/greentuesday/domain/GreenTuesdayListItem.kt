package com.johnnym.jackitemanimator.sample.greentuesday.domain

data class GreenTuesdayListItem(
        val id: String,
        val name: String,
        val price: Float,
        val discountPrice: Float,
        val imageUrl: String,
        val infoMessage: String?)
