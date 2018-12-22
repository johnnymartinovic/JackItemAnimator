package com.johnnym.recyclerviewdemo.recyclerviewfull.presentation.taxilist

import androidx.recyclerview.widget.RecyclerView
import com.johnnym.recyclerviewanimator.ItemMoveAndFadeAnimation
import com.johnnym.recyclerviewanimator.JackItemAnimation
import com.johnnym.recyclerviewanimator.JackItemAnimator

class CustomJackItemAnimator : JackItemAnimator() {

    override fun createRemoval(
            viewHolder: RecyclerView.ViewHolder
    ): JackItemAnimation {
        return ItemTranslateToRightAndFadeOutAnimation(viewHolder)
    }

    override fun createDisappearUnknownLastPosition(
            viewHolder: RecyclerView.ViewHolder
    ): JackItemAnimation {
        return ItemFadeOutAndScaleOutAnimation(viewHolder)
    }

    override fun createDisappearKnownLastPosition(
            viewHolder: RecyclerView.ViewHolder,
            deltaX: Int,
            deltaY: Int
    ): JackItemAnimation {
        return ItemMoveAndFadeAnimation(
                viewHolder,
                deltaX,
                0,
                deltaY,
                0,
                1f,
                1f)
    }

    override fun createAdd(
            viewHolder: RecyclerView.ViewHolder
    ): JackItemAnimation {
        return ItemFadeInFromLeftAnimation(viewHolder)
    }

    override fun createAppearKnownFirstPosition(
            viewHolder: RecyclerView.ViewHolder,
            deltaX: Int,
            deltaY: Int
    ): JackItemAnimation {
        return ItemMoveAndFadeAnimation(
                viewHolder,
                deltaX,
                0,
                deltaY,
                0,
                1f,
                1f)
    }

    override fun createMove(
            viewHolder: RecyclerView.ViewHolder,
            deltaX: Int,
            deltaY: Int
    ): JackItemAnimation {
        return ItemMoveAndFadeAnimation(
                viewHolder,
                deltaX,
                0,
                deltaY,
                0,
                1f,
                1f)
    }

    override fun createSameHolderItemChange(
            viewHolder: RecyclerView.ViewHolder,
            deltaX: Int,
            deltaY: Int,
            payloads: List<Any>
    ): JackItemAnimation {
        if (viewHolder is NormalItemViewHolder)
            return ItemMoveAndTaxiStatusChangeAnimation(
                    viewHolder,
                    deltaX,
                    0,
                    deltaY,
                    0,
                    payloads)
        else return ItemMoveAndFadeAnimation(
                viewHolder,
                deltaX,
                0,
                deltaY,
                0,
                1f,
                1f)
    }

    override fun createDifferentHolderItemChange(oldHolder: RecyclerView.ViewHolder, newHolder: RecyclerView.ViewHolder, deltaX: Int, deltaY: Int): Pair<JackItemAnimation, JackItemAnimation> {
        return Pair(
                ItemMoveAndFadeAnimation(oldHolder, 0, -deltaX, 0, -deltaY, 1f, 0f),
                ItemMoveAndFadeAnimation(newHolder, deltaX, 0, deltaY, 0, 0f, 1f))
    }
}