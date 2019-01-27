package com.johnnym.jackitemanimator.sample.travelino.presentation

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
import com.johnnym.jackitemanimator.sample.travelino.TravelinoModule
import com.johnnym.jackitemanimator.sample.travelino.presentation.list.TravelinoListAdapter
import kotlinx.android.synthetic.main.travelino_activity.*
import javax.inject.Inject

class TravelinoActivity : AppCompatActivity(),
        TravelinoContract.View {

    companion object {

        fun createIntent(context: Context): Intent {
            return Intent(context, TravelinoActivity::class.java)
        }
    }

    @Inject
    lateinit var presenter: TravelinoContract.Presenter

    private val travelinoItemsLoadingView: SwipeRefreshLayout by bindView(R.id.travelinoItemsLoadingView)
    private val travelinoItems: RecyclerView by bindView(R.id.travelinoItems)

    private lateinit var travelinoListAdapter: TravelinoListAdapter
    private lateinit var travelinoListLayoutManager: GridLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.travelino_activity)

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
            inflateMenu(R.menu.travelino_menu)
            setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.travelino_refresh_menu_item -> {
                        presenter.onRefreshButtonPressed()
                        true
                    }
                    else -> super.onOptionsItemSelected(item)
                }
            }
        }

        travelinoListAdapter = TravelinoListAdapter()
        travelinoItems.setHasFixedSize(true)
        travelinoItems.adapter = travelinoListAdapter
        travelinoListLayoutManager = GridLayoutManager(this, 2)
        travelinoListLayoutManager.spanSizeLookup = travelinoListSpanSizeLookup
        travelinoItems.layoutManager = travelinoListLayoutManager
        travelinoItems.addItemDecoration(MarginItemDecoration(
                resources.getDimensionPixelSize(R.dimen.margin_item_decoration_margin)))

        travelinoItemsLoadingView.isEnabled = false

        sampleApplication.sampleApplicationComponent
                .newTravelinoComponent(
                        TravelinoModule(
                                this))
                .inject(this)
    }

    override fun onDestroy() {
        super.onDestroy()

        presenter.viewDestroyed()
    }

    override fun showTravelinoListViewModel(viewModel: TravelinoListViewModel) {
        travelinoListAdapter.setItems(viewModel.itemList)
    }

    override fun showLoading() {
        travelinoItemsLoadingView.isRefreshing = true
    }

    override fun hideLoading() {
        travelinoItemsLoadingView.isRefreshing = false
    }

    private val travelinoListSpanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int {
            val style = travelinoListAdapter.getItem(position).style

            return when (style) {
                TravelinoItemViewModel.Style.FULL_WIDTH -> 2
                TravelinoItemViewModel.Style.HALF_WIDTH -> 1
            }
        }
    }
}
