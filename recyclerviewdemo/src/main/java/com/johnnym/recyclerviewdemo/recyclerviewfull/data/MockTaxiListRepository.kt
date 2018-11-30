package com.johnnym.recyclerviewdemo.recyclerviewfull.data

import com.johnnym.recyclerviewdemo.recyclerviewfull.TaxiListMockFactory
import com.johnnym.recyclerviewdemo.recyclerviewfull.domain.TaxiList
import io.reactivex.Single
import java.util.concurrent.TimeUnit

class MockTaxiListRepository : TaxiListRepository {

    private var getTaxiListRequestNumber = 0

    override fun getTaxiListSingle(): Single<TaxiList> {
        return Single
                .create<TaxiList> {
                    it.onSuccess(TaxiListMockFactory.createTaxiList(getTaxiListRequestNumber++))
                }
                .delay(1, TimeUnit.SECONDS)
    }
}
