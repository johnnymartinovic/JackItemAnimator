package com.johnnym.recyclerviewdemo.recyclerviewfull.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnCheckedChanged
import com.johnnym.recyclerviewdemo.R
import com.johnnym.recyclerviewdemo.common.rvdApplication
import com.johnnym.recyclerviewdemo.recyclerviewfull.TaxiListModule
import javax.inject.Inject
import android.support.v7.widget.DividerItemDecoration

class TaxiListActivity : AppCompatActivity(),
        TaxiListContract.View,
        TaxiListSortOptionsDialog.SortOptionSelectedListener {

    companion object {

        fun createIntent(context: Context): Intent {
            return Intent(context, TaxiListActivity::class.java)
        }

        private val SORT_OPTIONS_DIALOG_TAG = "sort_options_dialog_tag"
    }

    @BindView(R.id.toolbar) lateinit var toolbar: Toolbar
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
        taxiList.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        taxiListLoadingView.isEnabled = false

        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        rvdApplication.rvdApplicationComponent
                .newTaxiListComponent(TaxiListModule(this))
                .inject(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.taxi_list_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) =
            when (item.itemId) {
                android.R.id.home -> {
                    onBackPressed()
                    true
                }
                R.id.sort_menu_item -> {
                    presenter.onSortButtonPressed()
                    true
                }
                else -> super.onOptionsItemSelected(item)
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

    override fun showSortOptionsDialog(
            sortOptionList: List<String>,
            initiallySelectedSortOptionPosition: Int) {
        TaxiListSortOptionsDialog
                .createInstance(sortOptionList, initiallySelectedSortOptionPosition)
                .show(supportFragmentManager, SORT_OPTIONS_DIALOG_TAG)
    }

    override fun onSortOptionSelected(selectedSortOptionPosition: Int) {
        presenter.onSortOptionSelected(selectedSortOptionPosition)
    }

    @OnCheckedChanged(R.id.availability_visibility_switch)
    fun onAvailabilityVisibilitySwitchChecked(checked: Boolean) {
        presenter.availabilityVisibilitySwitchChecked(checked)
    }
}