package com.johnnym.jackitemanimator.sample.greentuesday.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.johnnym.jackitemanimator.sample.R
import com.johnnym.jackitemanimator.sample.common.sampleApplication
import com.johnnym.jackitemanimator.sample.greentuesday.GreenTuesdayModule
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