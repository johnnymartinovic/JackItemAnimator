package com.johnnym.jackitemanimator.sample.greentuesday.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.johnnym.jackitemanimator.sample.R
import com.johnnym.jackitemanimator.sample.common.sampleApplication
import com.johnnym.jackitemanimator.sample.greentuesday.GreenTuesdayModule
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

        sampleApplication.sampleApplicationComponent
                .newGreenTuesdayComponent(GreenTuesdayModule(
                        this))
                .inject(this)
    }

    override fun onDestroy() {
        super.onDestroy()

        presenter.viewDestroyed()
    }
}