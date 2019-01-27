package com.johnnym.jackitemanimator.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.johnnym.jackitemanimator.sample.diffutildemo.DiffUtilDemoActivity
import com.johnnym.jackitemanimator.sample.notifydemo.NotifyDemoActivity
import com.johnnym.jackitemanimator.sample.travelino.presentation.TravelinoActivity
import com.johnnym.jackitemanimator.sample.taxilist.presentation.TaxiListActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        taxiListButton.setOnClickListener {
            startActivity(TaxiListActivity.createIntent(this))
        }

        travelinoButton.setOnClickListener {
            startActivity(TravelinoActivity.createIntent(this))
        }

        insertItemDemoButton.setOnClickListener {
            startActivity(NotifyDemoActivity.createIntent(
                    this,
                    NotifyDemoActivity.NotifyDemoInitData(
                            NotifyDemoActivity.NotifyDemoInitData.DemoType.INSERT_ITEM)))
        }

        moveItemDemoButton.setOnClickListener {
            startActivity(NotifyDemoActivity.createIntent(
                    this,
                    NotifyDemoActivity.NotifyDemoInitData(
                            NotifyDemoActivity.NotifyDemoInitData.DemoType.MOVE_ITEM)))
        }

        removeItemRangeDemoButton.setOnClickListener {
            startActivity(NotifyDemoActivity.createIntent(
                    this,
                    NotifyDemoActivity.NotifyDemoInitData(
                            NotifyDemoActivity.NotifyDemoInitData.DemoType.REMOVE_ITEM_RANGE)))
        }

        changeItemDemoButton.setOnClickListener {
            startActivity(NotifyDemoActivity.createIntent(
                    this,
                    NotifyDemoActivity.NotifyDemoInitData(
                            NotifyDemoActivity.NotifyDemoInitData.DemoType.CHANGE_ITEM)))
        }

        changeItemWithPayloadDemoButton.setOnClickListener {
            startActivity(NotifyDemoActivity.createIntent(
                    this,
                    NotifyDemoActivity.NotifyDemoInitData(
                            NotifyDemoActivity.NotifyDemoInitData.DemoType.CHANGE_ITEM_WITH_PAYLOAD)))
        }

        diffUtilDemoButton.setOnClickListener {
            startActivity(DiffUtilDemoActivity.createIntent(this))
        }
    }
}
