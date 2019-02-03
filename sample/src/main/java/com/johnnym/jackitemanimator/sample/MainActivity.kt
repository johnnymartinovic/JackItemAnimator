package com.johnnym.jackitemanimator.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.johnnym.jackitemanimator.sample.travelino.presentation.TravelinoActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        travelinoButton.setOnClickListener {
            startActivity(TravelinoActivity.createIntent(this))
        }
    }
}
