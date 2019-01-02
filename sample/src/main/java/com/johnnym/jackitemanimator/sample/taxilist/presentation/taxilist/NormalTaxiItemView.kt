package com.johnnym.jackitemanimator.sample.taxilist.presentation.taxilist

import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.johnnym.jackitemanimator.sample.R
import com.johnnym.jackitemanimator.sample.common.binding.bindView

class NormalTaxiItemView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    val backgroundHelperView: View by bindView(R.id.background_helper_view)
    val statusBar: View by bindView(R.id.status_bar)
    val driverPhoto: ImageView by bindView(R.id.driver_photo)
    val driverName: TextView by bindView(R.id.driver_name)
    val starIcon: ImageView by bindView(R.id.star_icon)
    val stars: TextView by bindView(R.id.stars)
    val distanceIcon: ImageView by bindView(R.id.distance_icon)
    val distance: TextView by bindView(R.id.distance)

    init {
        inflate(context, R.layout.normal_taxi_list_item, this)
    }
}
