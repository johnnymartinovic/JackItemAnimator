package com.johnnym.jackitemanimator.sample.taxilist.presentation.taxilist

import androidx.recyclerview.widget.RecyclerView
import com.johnnym.jackitemanimator.ChangeJackItemAnimations
import com.johnnym.jackitemanimator.HolderAnimator
import com.johnnym.jackitemanimator.JackItemAnimation
import com.johnnym.jackitemanimator.JackItemAnimator
import com.johnnym.jackitemanimator.defaultanimations.ItemMoveAndFadeAnimation

class CustomJackItemAnimator : JackItemAnimator() {

    override fun createRemoval(
            holder: RecyclerView.ViewHolder
    ): JackItemAnimation {
        return ItemTranslateToRightAndFadeOutAnimation(holder)
    }

    override fun createDisappearUnknownLastPosition(
            holder: RecyclerView.ViewHolder
    ): JackItemAnimation {
        return ItemFadeOutAndScaleOutAnimation(holder)
    }

    override fun createDisappearKnownLastPosition(
            holder: RecyclerView.ViewHolder,
            deltaX: Int,
            deltaY: Int
    ): JackItemAnimation {
        return ItemMoveAndFadeAnimation(
                holder,
                deltaX,
                0,
                deltaY,
                0,
                1f,
                1f)
    }

    override fun createAdd(
            holder: RecyclerView.ViewHolder
    ): JackItemAnimation {
        return ItemFadeInFromLeftAnimation(holder)
    }

    override fun createAppearKnownFirstPosition(
            holder: RecyclerView.ViewHolder,
            deltaX: Int,
            deltaY: Int
    ): JackItemAnimation {
        return ItemMoveAndFadeAnimation(
                holder,
                deltaX,
                0,
                deltaY,
                0,
                1f,
                1f)
    }

    override fun createMove(
            holder: RecyclerView.ViewHolder,
            deltaX: Int,
            deltaY: Int
    ): JackItemAnimation {
        return ItemMoveAndFadeAnimation(
                holder,
                deltaX,
                0,
                deltaY,
                0,
                1f,
                1f)
    }

    override fun createSameHolderItemChange(
            holder: RecyclerView.ViewHolder,
            deltaX: Int,
            deltaY: Int,
            payloads: List<Any>
    ): JackItemAnimation {
        if (holder is NormalItemViewHolder)
            return ItemMoveAndTaxiStatusChangeAnimation(
                    holder,
                    deltaX,
                    0,
                    deltaY,
                    0,
                    payloads)
        else return ItemMoveAndFadeAnimation(
                holder,
                deltaX,
                0,
                deltaY,
                0,
                1f,
                1f)
    }

    override fun createDifferentHolderItemChange(
            oldHolder: RecyclerView.ViewHolder,
            newHolder: RecyclerView.ViewHolder,
            deltaX: Int,
            deltaY: Int
    ): ChangeJackItemAnimations {
        return ChangeJackItemAnimations(
                ItemMoveAndFadeAnimation(oldHolder, 0, -deltaX, 0, -deltaY, 1f, 0f),
                ItemMoveAndFadeAnimation(newHolder, deltaX, 0, deltaY, 0, 0f, 1f))
    }

    override fun handleAnimations(
            removeHolderAnimators: List<HolderAnimator>,
            disappearUnknownLastPositionHolderAnimators: List<HolderAnimator>,
            disappearKnownLastPositionHolderAnimators: List<HolderAnimator>,
            appearKnownFirstPositionHolderAnimators: List<HolderAnimator>,
            addHolderAnimators: List<HolderAnimator>,
            moveHolderAnimators: List<HolderAnimator>,
            changeHolderAnimators: List<HolderAnimator>) {
        val sortedRemoveHolderAnimators = removeHolderAnimators
                .sortedWith(Comparator { o1, o2 ->
                    o1.holder.itemView.y.compareTo(o2.holder.itemView.y)
                })

        sortedRemoveHolderAnimators.forEachIndexed { index, holderAnimator ->
            val animator = holderAnimator.animator

            animator.startDelay = index * 50L
            animator.start()
        }

        val firstStageAnimationsExist = sortedRemoveHolderAnimators.isNotEmpty()

        val secondStageDelay =
                if (firstStageAnimationsExist) 150L
                else 0L

        var secondStageAnimationsExist = false
        listOf(
                disappearUnknownLastPositionHolderAnimators,
                disappearKnownLastPositionHolderAnimators,
                appearKnownFirstPositionHolderAnimators,
                moveHolderAnimators,
                changeHolderAnimators)
                .forEach { holderAnimators ->
                    if (holderAnimators.isNotEmpty()) secondStageAnimationsExist = true

                    holderAnimators.forEach { holderAnimator ->
                        val animator = holderAnimator.animator

                        animator.startDelay = secondStageDelay
                        animator.start()
                    }
                }

        val thirdStageDelay =
                if (firstStageAnimationsExist && secondStageAnimationsExist) 300L
                else if (firstStageAnimationsExist) 150L
                else if (secondStageAnimationsExist) 150L
                else 0L

        val sortedAddHolderAnimators = addHolderAnimators
                .sortedWith(Comparator { o1, o2 ->
                    o1.holder.itemView.y.compareTo(o2.holder.itemView.y)
                })

        sortedAddHolderAnimators.forEachIndexed { index, holderAnimator ->
            val animator = holderAnimator.animator

            animator.startDelay = thirdStageDelay + index * 50L
            animator.start()
        }
    }
}
