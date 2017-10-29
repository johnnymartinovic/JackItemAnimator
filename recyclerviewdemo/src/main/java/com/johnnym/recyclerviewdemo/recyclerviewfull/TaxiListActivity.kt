package com.johnnym.recyclerviewdemo.recyclerviewfull

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnCheckedChanged
import com.johnnym.recyclerviewdemo.R
import com.johnnym.recyclerviewdemo.common.rvdApplication
import javax.inject.Inject

class TaxiListActivity: AppCompatActivity(), TaxiListContract.View {

    companion object {

        fun createIntent(context: Context): Intent {
            return Intent(context, TaxiListActivity::class.java)
        }
    }

    @BindView(R.id.availability_visibility_switch_container) lateinit var switchContainer: View
    @BindView(R.id.taxi_list) lateinit var taxiList: RecyclerView

    @Inject lateinit var presenter: TaxiListContract.Presenter

    private lateinit var taxiListAdapter: TaxiListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.taxi_list_activity)

        ButterKnife.bind(this)

        taxiListAdapter = TaxiListAdapter(this)
        taxiList.adapter = taxiListAdapter
        taxiList.layoutManager = LinearLayoutManager(this)

        rvdApplication.rvdApplicationComponent
                .newTaxiListComponent(TaxiListModule(this))
                .inject(this)
    }

    override fun showTaxiListPresentable(taxiListPresentable: TaxiListPresentable) {
        taxiListAdapter.setItems(taxiListPresentable.taxiListItemPresentables)
    }

    @OnCheckedChanged(R.id.availability_visibility_switch)
    fun onVisibilitySwitchChecked(checked: Boolean) {
        presenter.visibilitySwitchChecked(checked)
    }
}