package com.johnnym.recyclerviewdemo.recyclerviewfull.domain

data class TaxiListItem(
        val taxiId: String,
        val taxiStatus: TaxiStatus,
        val driverPhotoUrl: String,
        val driverName: String,
        val stars: Float,
        val distance: Float)

enum class TaxiStatus {
    AVAILABLE,
    OCCUPIED
}