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
import com.johnnym.recyclerviewdemo.R
import com.johnnym.recyclerviewdemo.common.rvdApplication
import com.johnnym.recyclerviewdemo.recyclerviewfull.TaxiListModule
import javax.inject.Inject
import android.support.v7.widget.DividerItemDecoration
import android.widget.Button
import android.widget.Switch
import com.johnnym.recyclerviewdemo.recyclerviewfull.domain.TaxiSortOption
import com.johnnym.recyclerviewdemo.recyclerviewfull.domain.TaxiStatusFilter

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
    @BindView(R.id.availability_visibility_switch) lateinit var availabilityVisibilitySwitch: Switch
    @BindView(R.id.taxi_list_loading_view) lateinit var taxiListLoadingView: SwipeRefreshLayout
    @BindView(R.id.taxi_list) lateinit var taxiList: RecyclerView
    @BindView(R.id.refresh_button) lateinit var refreshButton: Button

    @Inject lateinit var presenter: TaxiListContract.Presenter

    private lateinit var taxiListAdapter: TaxiListAdapter
    private lateinit var taxiListLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val initialTaxiStatusFilter = TaxiStatusFilter.ONLY_AVAILABLE
        val initialTaxiSortOption = TaxiSortOption.BY_DRIVER_NAME_ASCENDING

        setContentView(R.layout.taxi_list_activity)
        ButterKnife.bind(this)

        taxiListAdapter = TaxiListAdapter(this)
        taxiList.adapter = taxiListAdapter
        taxiListLayoutManager = LinearLayoutManager(this)
        taxiList.layoutManager = taxiListLayoutManager
        taxiList.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        taxiList.itemAnimator = TaxiListItemAnimator()

        taxiListLoadingView.isEnabled = false

        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        availabilityVisibilitySwitch.isChecked = initialTaxiStatusFilter == TaxiStatusFilter.NO_FILTER
        availabilityVisibilitySwitch.setOnCheckedChangeListener { _, isChecked -> presenter.availabilityVisibilitySwitchChecked(isChecked) }
        refreshButton.setOnClickListener { presenter.onRefreshButtonPressed() }

        rvdApplication.rvdApplicationComponent
                .newTaxiListComponent(TaxiListModule(
                        this,
                        initialTaxiStatusFilter,
                        initialTaxiSortOption))
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
}