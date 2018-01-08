package com.johnnym.recyclerviewdemo.recyclerviewfull

import com.johnnym.recyclerviewdemo.recyclerviewfull.domain.TaxiList
import com.johnnym.recyclerviewdemo.recyclerviewfull.domain.TaxiListItem
import com.johnnym.recyclerviewdemo.recyclerviewfull.domain.TaxiStatus

class TaxiListMockFactory {

    companion object {
        fun getTaxiList(): TaxiList =
                TaxiList(mutableListOf(
                        TaxiListItem(
                                "id_00",
                                TaxiStatus.AVAILABLE,
                                "https://pbs.twimg.com/media/CO0d2ZcUkAA6gjw.jpg",
                                "Brad Pitt",
                                3.545f,
                                2.7433f),
                        TaxiListItem(
                                "id_01",
                                TaxiStatus.OCCUPIED,
                                "https://vignette.wikia.nocookie.net/erwikia/images/c/c8/George_Clooney.jpg/revision/latest?cb=20120812171039",
                                "George Clooney",
                                4.23f,
                                8.644f),
                        TaxiListItem(
                                "id_02",
                                TaxiStatus.AVAILABLE,
                                "https://cdn.miramax.com/media/_versions/matt-damon_square_md.jpg",
                                "Matt Damon",
                                4.9321f,
                                0.5333f)))
    }
}