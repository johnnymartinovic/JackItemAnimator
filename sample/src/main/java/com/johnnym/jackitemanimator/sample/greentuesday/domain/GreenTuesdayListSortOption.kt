package com.johnnym.jackitemanimator.sample.greentuesday.domain

enum class GreenTuesdayListSortOption {

    BY_PRICE_ASCENDING {

        override fun createComparator(): Comparator<GreenTuesdayListItem> =
                PriceAscendingComparator()

        override fun getTaxiSortOptionName(): String {
            return "Lowest price first"
        }
    },
    BY_PRICE_DESCENDING {

        override fun createComparator(): Comparator<GreenTuesdayListItem> =
                PriceDescendingComparator()

        override fun getTaxiSortOptionName(): String {
            return "Highest price first"
        }
    },
    BY_DISCOUNT_PERCENTAGE_DESCENDING {

        override fun createComparator(): Comparator<GreenTuesdayListItem> =
                DiscountPercentageDescendingComparator()

        override fun getTaxiSortOptionName(): String {
            return "Best deals"
        }
    };

    abstract fun createComparator(): Comparator<GreenTuesdayListItem>

    abstract fun getTaxiSortOptionName(): String
}

class PriceAscendingComparator : Comparator<GreenTuesdayListItem> {

    override fun compare(o1: GreenTuesdayListItem, o2: GreenTuesdayListItem): Int =
            o1.price.compareTo(o2.price)
}

class PriceDescendingComparator : Comparator<GreenTuesdayListItem> {

    override fun compare(o1: GreenTuesdayListItem, o2: GreenTuesdayListItem): Int =
            o1.price.compareTo(o2.price).unaryMinus()
}

class DiscountPercentageDescendingComparator : Comparator<GreenTuesdayListItem> {

    override fun compare(o1: GreenTuesdayListItem, o2: GreenTuesdayListItem): Int =
            o1.originalPrice.compareTo(o2.originalPrice).unaryMinus()
}
