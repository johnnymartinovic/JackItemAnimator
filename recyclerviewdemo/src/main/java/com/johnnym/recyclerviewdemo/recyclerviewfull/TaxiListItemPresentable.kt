package com.johnnym.recyclerviewdemo.recyclerviewfull

data class TaxiListItemPresentable(
        val taxiStatus: TaxiStatus,
        val driverPhotoUrl: String,
        val driverName: String,
        val stars: Float,
        val distance: Float)

enum class TaxiStatus {
    AVAILABLE,
    OCCUPIED
}