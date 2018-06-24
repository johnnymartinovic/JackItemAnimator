package com.johnnym.recyclerviewdemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import com.johnnym.recyclerviewdemo.common.binding.bindView
import com.johnnym.recyclerviewdemo.recyclerviewfull.presentation.TaxiListActivity

class MainActivity : AppCompatActivity() {

    private val demoFullButton: Button by bindView(R.id.btn_demo_full)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        demoFullButton.setOnClickListener {
            startActivity(TaxiListActivity.createIntent(this))
        }
    }
}