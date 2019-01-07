package com.johnnym.jackitemanimator.sample.greentuesday.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.johnnym.jackitemanimator.sample.R
import com.johnnym.jackitemanimator.sample.common.binding.bindView
import com.johnnym.jackitemanimator.sample.common.sampleApplication
import com.johnnym.jackitemanimator.sample.common.views.MarginItemDecoration
import com.johnnym.jackitemanimator.sample.greentuesday.GreenTuesdayModule
import com.johnnym.jackitemanimator.sample.greentuesday.presentation.list.GreenTuesdayListAdapter
import kotlinx.android.synthetic.main.green_tuesday_activity.*
import javax.inject.Inject

class GreenTuesdayActivity : AppCompatActivity(),
        GreenTuesdayContract.View {

    companion object {

        fun createIntent(context: Context): Intent {
            return Intent(context, GreenTuesdayActivity::class.java)
        }
    }

    @Inject
    lateinit var presenter: GreenTuesdayContract.Presenter

    private val greenTuesdayItemsLoadingView: SwipeRefreshLayout by bindView(R.id.greenTuesdayItemsLoadingView)
    private val greenTuesdayItems: RecyclerView by bindView(R.id.greenTuesdayItems)

    private lateinit var greenTuesdayListAdapter: GreenTuesdayListAdapter
    private lateinit var greenTuesdayListLayoutManager: GridLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.green_tuesday_activity)

        window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

        root.setOnApplyWindowInsetsListener { _, insets ->
            statusBarBackground.layoutParams.height = insets.systemWindowInsetTop
            statusBarBackground.requestLayout()

            insets.consumeSystemWindowInsets()
        }

        with(toolbar) {
            setNavigationOnClickListener { onBackPressed() }
            inflateMenu(R.menu.green_tuesday_menu)
            setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.green_tuesday_grid_menu_item -> {
                        // TODO
                        true
                    }
                    R.id.green_tuesday_sort_menu_item -> {
                        // TODO
                        true
                    }
                    else -> super.onOptionsItemSelected(item)
                }
            }
        }

        greenTuesdayListAdapter = GreenTuesdayListAdapter()
        greenTuesdayItems.setHasFixedSize(true)
        greenTuesdayItems.adapter = greenTuesdayListAdapter
        greenTuesdayListLayoutManager = GridLayoutManager(this, 2)
        greenTuesdayListLayoutManager.spanSizeLookup = greenTuesdayListSpanSizeLookup
        greenTuesdayItems.layoutManager = greenTuesdayListLayoutManager
        greenTuesdayItems.addItemDecoration(MarginItemDecoration(
                resources.getDimensionPixelSize(R.dimen.margin_item_decoration_margin)))

        greenTuesdayItemsLoadingView.isEnabled = false

        sampleApplication.sampleApplicationComponent
                .newGreenTuesdayComponent(
                        GreenTuesdayModule(
                                this,
                                null))
                .inject(this)
    }

    override fun onDestroy() {
        super.onDestroy()

        presenter.viewDestroyed()
    }

    override fun showGreenTuesdayListViewModel(viewModel: GreenTuesdayListViewModel) {
        greenTuesdayListAdapter.setItems(viewModel.itemList)
    }

    override fun showLoading() {
        greenTuesdayItemsLoadingView.isRefreshing = true
    }

    override fun hideLoading() {
        greenTuesdayItemsLoadingView.isRefreshing = false
    }

    override fun showSortOptionsDialog(sortOptionList: List<String>, initiallySelectedSortOptionPosition: Int) {
        // TODO
    }

    private val greenTuesdayListSpanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int {
            val style = greenTuesdayListAdapter.getItem(position).style

            return when (style) {
                GreenTuesdayListItemViewModel.Style.FULL_WIDTH -> 2
                GreenTuesdayListItemViewModel.Style.HALF_WIDTH -> 1
            }
        }
    }
}
