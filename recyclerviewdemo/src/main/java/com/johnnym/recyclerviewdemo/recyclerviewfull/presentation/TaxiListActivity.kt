package com.johnnym.recyclerviewdemo.recyclerviewfull.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnCheckedChanged
import com.johnnym.recyclerviewdemo.R
import com.johnnym.recyclerviewdemo.common.rvdApplication
import com.johnnym.recyclerviewdemo.recyclerviewfull.TaxiListModule
import javax.inject.Inject

class TaxiListActivity : AppCompatActivity(), TaxiListContract.View {

    companion object {

        fun createIntent(context: Context): Intent {
            return Intent(context, TaxiListActivity::class.java)
        }
    }

    @BindView(R.id.taxi_list_loading_view) lateinit var taxiListLoadingView: SwipeRefreshLayout
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

        taxiListLoadingView.isEnabled = false

        rvdApplication.rvdApplicationComponent
                .newTaxiListComponent(TaxiListModule(this))
                .inject(this)
    }

    override fun onDestroy() {
        super.onDestroy()

        presenter.viewDestroyed()
    }

    override fun showTaxiListPresentable(taxiListPresentable: TaxiListPresentable) {
        taxiListAdapter.setItems(taxiListPresentable.taxiListItemPresentables)
    }

    override fun showLoading() {
        taxiListLoadingView.isRefreshing = true
    }

    override fun hideLoading() {
        taxiListLoadingView.isRefreshing = false
    }

    @OnCheckedChanged(R.id.availability_visibility_switch)
    fun onAvailabilityVisibilitySwitchChecked(checked: Boolean) {
        presenter.availabilityVisibilitySwitchChecked(checked)
    }
}