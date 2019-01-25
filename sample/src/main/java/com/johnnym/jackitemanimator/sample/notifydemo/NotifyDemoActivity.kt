package com.johnnym.jackitemanimator.sample.notifydemo

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
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
import com.johnnym.jackitemanimator.sample.travelino.domain.TravelinoListItem
import com.johnnym.jackitemanimator.sample.travelino.presentation.TravelinoListViewModelMapper
import kotlinx.android.parcel.Parcelize

class NotifyDemoActivity : AppCompatActivity() {

    companion object {

        private const val EXTRA_NOTIFY_DEMO_INIT_DATA = "EXTRA_NOTIFY_DEMO_INIT_DATA"

        fun createIntent(context: Context, initData: NotifyDemoInitData): Intent {
            val intent = Intent(context, NotifyDemoActivity::class.java)
            intent.putExtra(EXTRA_NOTIFY_DEMO_INIT_DATA, initData)
            return intent
        }
    }

    private val root: ConstraintLayout by bindView(R.id.root)
    private val statusBarBackground: View by bindView(R.id.statusBarBackground)
    private val toolbar: Toolbar by bindView(R.id.toolbar)
    private val itemsView: RecyclerView by bindView(R.id.itemsRecyclerView)
    private val playButton: View by bindView(R.id.playButton)

    private val mapper = TravelinoListViewModelMapper()

    private lateinit var listAdapter: NotifyDemoListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.notify_demo_activity)

        window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

        root.setOnApplyWindowInsetsListener { _, insets ->
            statusBarBackground.layoutParams.height = insets.systemWindowInsetTop
            statusBarBackground.requestLayout()

            val insetBottom = insets.systemWindowInsetBottom
            itemsView.updatePadding(bottom = insetBottom)
            playButton.translationY -= insetBottom

            insets.consumeSystemWindowInsets()
        }

        toolbar.setNavigationOnClickListener { onBackPressed() }

        listAdapter = NotifyDemoListAdapter()
        itemsView.setHasFixedSize(true)
        itemsView.adapter = listAdapter
        itemsView.layoutManager = LinearLayoutManager(this)
        itemsView.addItemDecoration(MarginItemDecoration(
                resources.getDimensionPixelSize(R.dimen.margin_item_decoration_margin)))
        listAdapter.setItems(items.map { mapper.map(it) })

        playButton.setOnClickListener {
            playButton.setOnClickListener(null)
            playButton.animate()
                    .scaleX(0f)
                    .scaleY(0f)
                    .rotation(360f)
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator?) {
                            playButton.visibility = View.GONE
                        }
                    })

            val initData: NotifyDemoInitData = intent.getParcelableExtra(EXTRA_NOTIFY_DEMO_INIT_DATA)

            when (initData.demoType) {
                NotifyDemoInitData.DemoType.INSERT_ITEM -> {
                    val newItem = TravelinoListItem(
                            "id_08",
                            "Hawaii",
                            120,
                            150,
                            TravelinoMockFactory.createImageUrl("prSogOoFmkw"),
                            null)
                    listAdapter.insertItem(mapper.map(newItem), 2)
                }
                NotifyDemoInitData.DemoType.MOVE_ITEM -> {
                    listAdapter.moveItem(3, 1)
                }
                NotifyDemoInitData.DemoType.REMOVE_ITEM_RANGE -> {
                    listAdapter.removeItemRange(2, 2)
                }
                NotifyDemoInitData.DemoType.CHANGE_ITEM -> {
                    listAdapter.changeItem(1, "Eiffel tower is here!")
                }
            }
        }
    }

    private val items = listOf(
            TravelinoListItem(
                    "id_00",
                    "Zagreb",
                    70,
                    92,
                    TravelinoMockFactory.createImageUrl("ZINC3joF-JQ"),
                    null),
            TravelinoListItem(
                    "id_01",
                    "Paris",
                    52,
                    74,
                    TravelinoMockFactory.createImageUrl("Q0-fOL2nqZc"),
                    "Paris is in France."),
            TravelinoListItem(
                    "id_02",
                    "New York",
                    60,
                    95,
                    TravelinoMockFactory.createImageUrl("UExx0KnnkjY"),
                    null),
            TravelinoListItem(
                    "id_03",
                    "London",
                    30,
                    35,
                    TravelinoMockFactory.createImageUrl("tZDtyUrYrFU"),
                    null),
            TravelinoListItem(
                    "id_04",
                    "Sidney",
                    102,
                    134,
                    TravelinoMockFactory.createImageUrl("DLbCETd599Y"),
                    "Sidney is in Australia."),
            TravelinoListItem(
                    "id_05",
                    "Berlin",
                    48,
                    64,
                    TravelinoMockFactory.createImageUrl("fv0yV5-Pbjc"),
                    null),
            TravelinoListItem(
                    "id_06",
                    "Rome",
                    42,
                    48,
                    TravelinoMockFactory.createImageUrl("0Bs3et8FYyg"),
                    "Rome is in Italy."),
            TravelinoListItem(
                    "id_07",
                    "Cuba",
                    99,
                    113,
                    TravelinoMockFactory.createImageUrl("RqMIFcDLeos"),
                    null))

    @Parcelize
    data class NotifyDemoInitData(
            val demoType: DemoType
    ) : Parcelable {

        enum class DemoType {
            INSERT_ITEM,
            MOVE_ITEM,
            REMOVE_ITEM_RANGE,
            CHANGE_ITEM
        }
    }
}
