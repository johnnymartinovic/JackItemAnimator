package com.johnnym.jackitemanimator.sample.diffutildemo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.johnnym.jackitemanimator.sample.R
import com.johnnym.jackitemanimator.sample.common.binding.bindView
import com.johnnym.jackitemanimator.sample.common.views.MarginItemDecoration
import com.johnnym.jackitemanimator.sample.travelino.data.TravelinoMockFactory
import com.johnnym.jackitemanimator.sample.travelino.presentation.TravelinoListViewModelMapper

class DiffUtilDemoActivity : AppCompatActivity() {

    companion object {

        fun createIntent(context: Context): Intent =
                Intent(context, DiffUtilDemoActivity::class.java)
    }

    private val root: ConstraintLayout by bindView(R.id.root)
    private val statusBarBackground: View by bindView(R.id.statusBarBackground)
    private val toolbar: Toolbar by bindView(R.id.toolbar)
    private val itemsView: RecyclerView by bindView(R.id.itemsRecyclerView)
    private val refreshButton: View by bindView(R.id.refreshButton)

    private val adapter = DiffUtilDemoAdapter()
    private val mapper = TravelinoListViewModelMapper()

    private lateinit var layoutManager: LinearLayoutManager

    private var nextInstanceNumber = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.diff_util_demo_activity)

        window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

        root.setOnApplyWindowInsetsListener { _, insets ->
            statusBarBackground.layoutParams.height = insets.systemWindowInsetTop
            statusBarBackground.requestLayout()

            val insetBottom = insets.systemWindowInsetBottom
            itemsView.updatePadding(bottom = insetBottom)
            refreshButton.translationY -= insetBottom

            insets.consumeSystemWindowInsets()
        }

        toolbar.setNavigationOnClickListener { onBackPressed() }

        itemsView.setHasFixedSize(true)
        itemsView.adapter = adapter
        layoutManager = LinearLayoutManager(this)
        itemsView.layoutManager = layoutManager
        itemsView.addItemDecoration(MarginItemDecoration(
                resources.getDimensionPixelSize(R.dimen.margin_item_decoration_margin)))

        refreshButton.setOnClickListener {
            changeAdapterData()
        }

        changeAdapterData()
    }

    private fun changeAdapterData() {
        adapter.setItems(TravelinoMockFactory.createTravelinoItemList(nextInstanceNumber).map { mapper.map(it) })
        nextInstanceNumber = (nextInstanceNumber + 1) % 2
    }
}
