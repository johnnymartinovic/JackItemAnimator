package com.johnnym.recyclerviewdemo.recyclerviewfull.presentation.taxilist

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

class TaxiListItemDecoration(
        private val columnNumber: Int,
        private val spacingBetweenElements: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val itemCount = parent.adapter.itemCount
        val itemPosition = parent.getChildAdapterPosition(view)

        if (itemPosition == RecyclerView.NO_POSITION) {
            return
        }

        val numberOfRows = calculateNumberOfRows(itemCount)

        // TODO do this with GridLayoutManager.LayoutParams
        val isItemInTheFirstRow = itemPosition < columnNumber
        val isItemInTheLastRow = itemPosition > (numberOfRows - 1) * columnNumber - 1
        val isItemOnTheLeft = itemPosition % columnNumber == 0
        val isItemOnTheRight = itemPosition % columnNumber == columnNumber - 1

        outRect.top =
                if (isItemInTheFirstRow) spacingBetweenElements
                else spacingBetweenElements / 2

        outRect.bottom =
                if (isItemInTheLastRow) spacingBetweenElements
                else spacingBetweenElements / 2

        outRect.left =
                if (isItemOnTheLeft) spacingBetweenElements
                else spacingBetweenElements / 2

        outRect.right =
                if (isItemOnTheRight) spacingBetweenElements
                else spacingBetweenElements / 2
    }

    private fun calculateNumberOfRows(itemCount: Int): Int {
        var numberOfRows: Int = itemCount / columnNumber

        if (itemCount % columnNumber > 0) {
            numberOfRows++
        }

        return numberOfRows
    }
}
