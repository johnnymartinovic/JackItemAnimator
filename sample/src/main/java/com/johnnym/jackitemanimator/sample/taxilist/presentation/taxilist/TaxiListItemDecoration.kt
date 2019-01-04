package com.johnnym.jackitemanimator.sample.taxilist.presentation.taxilist

import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView
import android.view.View

class TaxiListItemDecoration(
        private val spacingBetweenElements: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.top = spacingBetweenElements / 2
        outRect.bottom = spacingBetweenElements / 2
        outRect.left = spacingBetweenElements / 2
        outRect.right = spacingBetweenElements / 2
    }
}