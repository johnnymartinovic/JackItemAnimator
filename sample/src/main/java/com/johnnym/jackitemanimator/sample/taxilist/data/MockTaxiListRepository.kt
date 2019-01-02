package com.johnnym.jackitemanimator.sample.taxilist.data

import com.johnnym.jackitemanimator.sample.taxilist.TaxiListMockFactory
import com.johnnym.jackitemanimator.sample.taxilist.domain.TaxiList
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
