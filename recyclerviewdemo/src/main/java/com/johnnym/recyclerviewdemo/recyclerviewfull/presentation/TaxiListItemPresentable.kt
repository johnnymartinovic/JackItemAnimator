package com.johnnym.recyclerviewdemo.recyclerviewfull.presentation

import com.johnnym.recyclerviewdemo.recyclerviewfull.domain.TaxiStatus

data class TaxiListItemPresentable(
        val taxiId: String,
        val taxiStatus: TaxiStatus,
        val driverPhotoUrl: String,
        val driverName: String,
        val stars: Float,
        val distance: Float)