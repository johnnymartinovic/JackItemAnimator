package com.johnnym.recyclerviewdemo.recyclerviewfull.presentation.taxilist

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import com.johnnym.recyclerviewdemo.R
import com.johnnym.recyclerviewdemo.common.binding.bindView

class SquareTaxiItemView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    val statusBar: View by bindView(R.id.status_bar)
    val driverPhoto: ImageView by bindView(R.id.driver_photo)

    init {
        inflate(context, R.layout.square_taxi_list_item, this)
    }
}
