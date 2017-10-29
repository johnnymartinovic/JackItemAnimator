package com.johnnym.recyclerviewdemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import butterknife.BindView
import butterknife.ButterKnife
import com.johnnym.recyclerviewdemo.recyclerviewfull.TaxiListActivity

class MainActivity : AppCompatActivity() {

    @BindView(R.id.btn_demo_full) lateinit var demoFullButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)

        demoFullButton.setOnClickListener {
            startActivity(TaxiListActivity.createIntent(this))
        }
    }
}