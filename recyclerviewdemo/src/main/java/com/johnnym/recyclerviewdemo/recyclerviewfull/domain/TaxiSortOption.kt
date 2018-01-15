package com.johnnym.recyclerviewdemo.recyclerviewfull.domain

enum class TaxiSortOption {

    BY_DRIVER_NAME_ASCENDING {

        override fun createComparator(): Comparator<TaxiListItem> =
                DriverNameAscendingComparator()

        override fun getTaxiSortOptionName(): String {
            return "By name (ascending)"
        }
    },
    BY_DISTANCE_ASCENDING {

        override fun createComparator(): Comparator<TaxiListItem> =
                DistanceAscendingComparator()

        override fun getTaxiSortOptionName(): String {
            return "By distance (ascending)"
        }
    },
    BY_STARS_DESCENDING {

        override fun createComparator(): Comparator<TaxiListItem> =
                StarsDescendingComparator()

        override fun getTaxiSortOptionName(): String {
            return "By stars (descending)"
        }
    };

    abstract fun createComparator(): Comparator<TaxiListItem>

    abstract fun getTaxiSortOptionName(): String
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