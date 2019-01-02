package com.johnnym.jackitemanimator.sample.taxilist.presentation

import com.johnnym.jackitemanimator.sample.taxilist.domain.TaxiStatus

data class TaxiListItemViewModel(
        val taxiId: String,
        val taxiStatus: TaxiStatus,
        val driverPhotoUrl: String,
        val driverName: String,
        val stars: Float,
        val distance: Float)