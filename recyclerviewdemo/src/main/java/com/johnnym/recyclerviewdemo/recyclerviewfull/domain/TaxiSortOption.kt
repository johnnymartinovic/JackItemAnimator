package com.johnnym.recyclerviewdemo.recyclerviewfull.domain

enum class TaxiSortOption {

    BY_DRIVER_NAME_ASCENDING {
        override fun createComparator(): Comparator<TaxiListItem> =
                DriverNameAscendingComparator()
    },
    BY_DISTANCE_ASCENDING {
        override fun createComparator(): Comparator<TaxiListItem> =
                DistanceAscendingComparator()
    },
    BY_STARS_DESCENDING {
        override fun createComparator(): Comparator<TaxiListItem> =
                StarsDescendingComparator()
    };

    abstract fun createComparator(): Comparator<TaxiListItem>
}


class DriverNameAscendingComparator : Comparator<TaxiListItem> {

    override fun compare(o1: TaxiListItem, o2: TaxiListItem): Int =
            o1.driverName.compareTo(o2.driverName)
}

class DistanceAscendingComparator : Comparator<TaxiListItem> {

    override fun compare(o1: TaxiListItem, o2: TaxiListItem): Int =
            o1.distance.compareTo(o2.distance)
}

class StarsDescendingComparator : Comparator<TaxiListItem> {

    override fun compare(o1: TaxiListItem, o2: TaxiListItem): Int =
            o1.stars.compareTo(o2.stars).unaryMinus()
}