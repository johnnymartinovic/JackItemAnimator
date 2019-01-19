package com.johnnym.jackitemanimator.sample.travelino.presentation.list

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.github.rstanic12.resourceful.bindDimen
import com.johnnym.jackitemanimator.sample.R
import com.johnnym.jackitemanimator.sample.common.binding.bindView
import com.johnnym.jackitemanimator.sample.common.views.CardViewOutlineProvider

class TravelinoNormalItemView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val itemElevation by bindDimen(R.dimen.travelino_item_elevation)

    val image: ImageView by bindView(R.id.image)
    val title: TextView by bindView(R.id.title)
    val price: TextView by bindView(R.id.price)
    val originalPrice: TextView by bindView(R.id.originalPrice)
    val discountPercentage: TextView by bindView(R.id.discountPercentage)
    val alarmMessage: TextView by bindView(R.id.alarmMessage)

    init {
        inflate(context, R.layout.travelino_normal_item, this)

        outlineProvider = CardViewOutlineProvider()
        clipToOutline = true

        elevation = itemElevation
    }
}
