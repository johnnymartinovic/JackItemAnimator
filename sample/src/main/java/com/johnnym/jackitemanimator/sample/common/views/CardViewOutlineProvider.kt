package com.johnnym.jackitemanimator.sample.common.views

import android.graphics.Outline
import android.view.View
import android.view.ViewOutlineProvider
import com.johnnym.jackitemanimator.sample.R

class CardViewOutlineProvider : ViewOutlineProvider() {
    override fun getOutline(view: View, outline: Outline) {
        val radius = view.resources.getDimension(R.dimen.travelino_item_radius)
        outline.setRoundRect(0, 0, view.width, view.height, radius)
    }
}
