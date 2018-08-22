package com.johnnym.recyclerviewdemo.recyclerviewfull.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.johnnym.recyclerviewdemo.R
import com.johnnym.recyclerviewdemo.common.rvdApplication
import com.johnnym.recyclerviewdemo.recyclerviewfull.TaxiListModule
import com.johnnym.recyclerviewdemo.recyclerviewfull.domain.TaxiSortOption
import com.johnnym.recyclerviewdemo.recyclerviewfull.domain.TaxiStatusFilter
import com.johnnym.recyclerviewdemo.recyclerviewfull.presentation.taxilist.TaxiListAdapter
import com.johnnym.recyclerviewdemo.recyclerviewfull.presentation.taxilist.TaxiListItemAnimator
import com.johnnym.recyclerviewdemo.recyclerviewfull.presentation.taxilist.TaxiListItemDecoration
import kotlinx.android.synthetic.main.taxi_list_activity.*
import javax.inject.Inject

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

    @Inject
    lateinit var presenter: TaxiListContract.Presenter

    private lateinit var taxiListAdapter: TaxiListAdapter
    private lateinit var taxiListLayoutManager: GridLayoutManager
    private lateinit var taxiListItemDecoration: RecyclerView.ItemDecoration

    private var currentTaxiListItemColumnNumber: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val initialTaxiStatusFilter = TaxiStatusFilter.ONLY_AVAILABLE
        val initialTaxiSortOption = TaxiSortOption.BY_DRIVER_NAME_ASCENDING

        setContentView(R.layout.taxi_list_activity)

        taxiListAdapter = TaxiListAdapter(this)
        setTaxiListAdapterViewType()
        taxiList.setHasFixedSize(true)
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

        val gridSpinnerArray = arrayListOf("1", "2", "3")
        val gridSpinnerArrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, gridSpinnerArray)
        gridSpinner.adapter = gridSpinnerArrayAdapter
        gridSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                currentTaxiListItemColumnNumber = position + 1
                setTaxiListAdapterViewType()
                refreshTaxiListItemDecoration()
            }
        }
        gridSpinner.setSelection(currentTaxiListItemColumnNumber - 1)

        val itemAnimatorSpinnerArray = arrayListOf("Default", "Custom")
        val itemAnimatorSpinnerArrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, itemAnimatorSpinnerArray)
        itemAnimatorSpinner.adapter = itemAnimatorSpinnerArrayAdapter
        itemAnimatorSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position == 0) taxiList.itemAnimator = DefaultItemAnimator()
                else if (position == 1) taxiList.itemAnimator = TaxiListItemAnimator()
            }
        }
        itemAnimatorSpinner.setSelection(1)

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
        taxiList.scrollToPosition(0)
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
            TaxiListAdapter.ViewType.NORMAL
        } else {
            TaxiListAdapter.ViewType.SQUARE
        }
        taxiListAdapter.setViewType(newViewType)
    }

    private fun refreshTaxiListItemDecoration() {
        taxiList.removeItemDecoration(taxiListItemDecoration)
        taxiListItemDecoration = createTaxiListItemDecoration()
        taxiList.addItemDecoration(taxiListItemDecoration)
    }

    private fun createTaxiListItemDecoration(): RecyclerView.ItemDecoration {
        return TaxiListItemDecoration(resources.getDimensionPixelSize(R.dimen.taxi_list_item_decoration_spacing))
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