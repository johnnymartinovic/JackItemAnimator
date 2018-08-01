package com.johnnym.recyclerviewdemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.johnnym.recyclerviewdemo.recyclerviewfull.presentation.TaxiListActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fullDemoButton.setOnClickListener {
            startActivity(TaxiListActivity.createIntent(this))
        }
    }
}