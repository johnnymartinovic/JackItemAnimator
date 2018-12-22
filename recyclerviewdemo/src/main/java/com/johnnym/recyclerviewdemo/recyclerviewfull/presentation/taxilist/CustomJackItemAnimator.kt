package com.johnnym.recyclerviewdemo.recyclerviewfull.presentation.taxilist

import android.animation.Animator
import androidx.recyclerview.widget.RecyclerView
import com.johnnym.recyclerviewanimator.JackItemAnimation
import com.johnnym.recyclerviewanimator.JackItemAnimator
import com.johnnym.recyclerviewanimator.defaultanimations.ItemMoveAndFadeAnimation

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

    override fun handleAnimations(
            removeAnimators: List<Animator>,
            disappearUnknownLastPositionAnimators: List<Animator>,
            disappearKnownLastPositionAnimators: List<Animator>,
            appearKnownFirstPositionAnimators: List<Animator>,
            addAnimators: List<Animator>,
            moveAnimators: List<Animator>,
            changeAnimators: List<Animator>) {
        removeAnimators.forEachIndexed { index, animator ->
            animator.startDelay = index * 100L
            animator.start()
        }

        listOf(
                disappearUnknownLastPositionAnimators,
                disappearKnownLastPositionAnimators,
                appearKnownFirstPositionAnimators,
                moveAnimators,
                changeAnimators
        ).forEach { animators ->
            animators.forEach {
                it.startDelay = 500
                it.start()
            }
        }

        addAnimators.forEachIndexed { index, animator ->
            animator.startDelay = 1000 + index * 100L
            animator.start()
        }
    }
}
