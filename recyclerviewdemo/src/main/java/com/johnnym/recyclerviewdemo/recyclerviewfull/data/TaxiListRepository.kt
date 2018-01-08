package com.johnnym.recyclerviewdemo.recyclerviewfull.data;

import com.johnnym.recyclerviewdemo.recyclerviewfull.domain.TaxiList
import io.reactivex.Single

interface TaxiListRepository {

    fun getTaxiListSingle(): Single<TaxiList>
}
