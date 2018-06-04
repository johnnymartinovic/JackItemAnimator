package com.johnnym.recyclerviewdemo.recyclerviewfull.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.*
import android.view.Menu
import android.view.MenuItem
import butterknife.BindView
import butterknife.ButterKnife
import com.johnnym.recyclerviewdemo.R
import com.johnnym.recyclerviewdemo.common.rvdApplication
import com.johnnym.recyclerviewdemo.recyclerviewfull.TaxiListModule
import javax.inject.Inject
import android.widget.Button
import android.widget.Switch
import com.johnnym.recyclerviewdemo.recyclerviewfull.domain.TaxiSortOption
import com.johnnym.recyclerviewdemo.recyclerviewfull.domain.TaxiStatusFilter
import com.johnnym.recyclerviewdemo.recyclerviewfull.presentation.taxilist.*

class TaxiListActivity : AppCompatActivity(),
        TaxiListContract.View,
        TaxiListSortOptionsDialog.SortOptionSelectedListener {

    companion object {

        fun createIntent(context: Context): Intent {
            return Intent(context, TaxiListActivity::class.java)
        }

        private const val SORT_OPTIONS_DIALOG_TAG = "sort_options_dialog_tag"

        private const val MAX_COLUMNS = 3
    }

    @BindView(R.id.toolbar) lateinit var toolbar: Toolbar
    @BindView(R.id.availability_visibility_switch) lateinit var availabilityVisibilitySwitch: Switch
    @BindView(R.id.taxi_list_loading_view) lateinit var taxiListLoadingView: SwipeRefreshLayout
    @BindView(R.id.taxi_list) lateinit var taxiList: RecyclerView
    @BindView(R.id.refresh_button) lateinit var refreshButton: Button
    @BindView(R.id.change_grid_button) lateinit var changeGridButton: Button

    @Inject lateinit var presenter: TaxiListContract.Presenter

    private lateinit var taxiListAdapter: TaxiListAdapter
    private lateinit var taxiListLayoutManager: GridLayoutManager
    private lateinit var taxiListItemDecoration: RecyclerView.ItemDecoration

    private var currentTaxiListItemColumnNumber: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val initialTaxiStatusFilter = TaxiStatusFilter.ONLY_AVAILABLE
        val initialTaxiSortOption = TaxiSortOption.BY_DRIVER_NAME_ASCENDING

        setContentView(R.layout.taxi_list_activity)
        ButterKnife.bind(this)

        taxiListAdapter = TaxiListAdapter(this)
        setTaxiListAdapterViewType()
        taxiList.adapter = taxiListAdapter
        taxiListLayoutManager = GridLayoutManager(this, calculateSpanCount())
        taxiListLayoutManager.spanSizeLookup = taxiListItemSpanSizeLookup
        taxiList.layoutManager = taxiListLayoutManager
        taxiList.itemAnimator = TaxiListItemAnimator()
        taxiListItemDecoration = createTaxiListItemDecoration()
        taxiList.addItemDecoration(taxiListItemDecoration)

        taxiListLoadingView.isEnabled = false

        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        availabilityVisibilitySwitch.isChecked = initialTaxiStatusFilter == TaxiStatusFilter.NO_FILTER
        availabilityVisibilitySwitch.setOnCheckedChangeListener { _, isChecked -> presenter.availabilityVisibilitySwitchChecked(isChecked) }
        refreshButton.setOnClickListener { presenter.onRefreshButtonPressed() }
        changeGridButton.setOnClickListener {
            currentTaxiListItemColumnNumber = currentTaxiListItemColumnNumber % MAX_COLUMNS + 1
            setTaxiListAdapterViewType()
            refreshTaxiListItemDecoration()
        }

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

    override fun showTaxiListViewModel(taxiListViewModel: TaxiListViewModel) {
        taxiListAdapter.setItems(taxiListViewModel.taxiListItemViewModels)
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

    private val taxiListItemSpanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int {
            return calculateSpanSize()
        }
    }

    private fun setTaxiListAdapterViewType() {
        val newViewType = if (currentTaxiListItemColumnNumber == 1) {
            TaxiListAdapter.NORMAL_VIEW_TYPE
        } else {
            TaxiListAdapter.SQUARE_VIEW_TYPE
        }
        taxiListAdapter.setViewType(newViewType)
    }

    private fun refreshTaxiListItemDecoration() {
        taxiList.removeItemDecoration(taxiListItemDecoration)
        taxiListItemDecoration = createTaxiListItemDecoration()
        taxiList.addItemDecoration(taxiListItemDecoration)
    }

    private fun createTaxiListItemDecoration(): RecyclerView.ItemDecoration {
        return TaxiListItemDecoration(
                currentTaxiListItemColumnNumber,
                resources.getDimension(R.dimen.taxi_list_item_decoration_spacing).toInt())
    }

    private fun calculateSpanCount(): Int {
        var spanCount = 1

        for (num in 1..MAX_COLUMNS)
            spanCount *= num

        return spanCount
    }

    private fun calculateSpanSize(): Int {
        return calculateSpanCount() / currentTaxiListItemColumnNumber
    }
}