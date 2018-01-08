package com.johnnym.recyclerviewdemo.recyclerviewfull.data

import com.johnnym.recyclerviewdemo.recyclerviewfull.TaxiListMockFactory
import com.johnnym.recyclerviewdemo.recyclerviewfull.domain.TaxiList
import io.reactivex.Single
import java.util.concurrent.TimeUnit

class MockTaxiListRepository : TaxiListRepository {

    override fun getTaxiListSingle(): Single<TaxiList> {
        return Single
                .create<TaxiList> {
                    it.onSuccess(TaxiListMockFactory.getTaxiList())
                }
                .delay(2, TimeUnit.SECONDS)
    }
}
