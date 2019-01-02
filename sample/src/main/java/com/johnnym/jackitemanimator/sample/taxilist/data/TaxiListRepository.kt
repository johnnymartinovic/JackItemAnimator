package com.johnnym.jackitemanimator.sample.taxilist.data;

import com.johnnym.jackitemanimator.sample.taxilist.domain.TaxiList
import io.reactivex.Single

interface TaxiListRepository {

    fun getTaxiListSingle(): Single<TaxiList>
}
