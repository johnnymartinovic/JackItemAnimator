package com.johnnym.jackitemanimator.sample.greentuesday.presentation.list

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GreenTuesdayListItemDecoration(
        private val spacingBetweenElements: Int,
        private val firstElementTopMargin: Int,
        private val lastElementBottomMargin: Int,
        private val leftMargin: Int,
        private val rightMargin: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val itemPosition = parent.getChildAdapterPosition(view)

        when (itemPosition) {
            0 -> {
                outRect.top = firstElementTopMargin
                outRect.bottom = spacingBetweenElements / 2
            }
            parent.adapter!!.itemCount - 1 -> {
                outRect.top = spacingBetweenElements / 2
                outRect.bottom = lastElementBottomMargin
            }
            else -> {
                outRect.top = spacingBetweenElements / 2
                outRect.bottom = spacingBetweenElements / 2
            }
        }
        outRect.left = leftMargin
        outRect.right = rightMargin
    }
}