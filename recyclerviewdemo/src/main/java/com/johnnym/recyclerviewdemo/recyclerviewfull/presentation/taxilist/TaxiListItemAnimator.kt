package com.johnnym.recyclerviewdemo.recyclerviewfull.presentation.taxilist

import android.animation.*
import android.animation.AnimatorSet
import androidx.recyclerview.widget.RecyclerView

class TaxiListItemAnimator : RecyclerView.ItemAnimator() {

    private val pendingRemoves: MutableMap<RecyclerView.ViewHolder, ItemAnimation> = HashMap()
    private val pendingDisappearUnknownLastPosition: MutableMap<RecyclerView.ViewHolder, ItemAnimation> = HashMap()
    private val pendingDisappearKnownLastPosition: MutableMap<RecyclerView.ViewHolder, ItemAnimation> = HashMap()
    private val pendingAppearKnownFirstPosition: MutableMap<RecyclerView.ViewHolder, ItemAnimation> = HashMap()
    private val pendingAdds: MutableMap<RecyclerView.ViewHolder, ItemAnimation> = HashMap()
    private val pendingMoves: MutableMap<RecyclerView.ViewHolder, ItemAnimation> = HashMap()
    private val pendingChanges: MutableMap<RecyclerView.ViewHolder, ItemAnimation> = HashMap()

    private val activeRemoves: MutableMap<RecyclerView.ViewHolder, ItemAnimation> = HashMap()
    private val activeDisappearUnknownLastPosition: MutableMap<RecyclerView.ViewHolder, ItemAnimation> = HashMap()
    private val activeDisappearKnownLastPosition: MutableMap<RecyclerView.ViewHolder, ItemAnimation> = HashMap()
    private val activeAppearKnownFirstPosition: MutableMap<RecyclerView.ViewHolder, ItemAnimation> = HashMap()
    private val activeAdds: MutableMap<RecyclerView.ViewHolder, ItemAnimation> = HashMap()
    private val activeMoves: MutableMap<RecyclerView.ViewHolder, ItemAnimation> = HashMap()
    private val activeChanges: MutableMap<RecyclerView.ViewHolder, ItemAnimation> = HashMap()

    private val pendingAnimationsMapList = listOf(
            pendingRemoves,
            pendingDisappearUnknownLastPosition,
            pendingDisappearKnownLastPosition,
            pendingAppearKnownFirstPosition,
            pendingAdds,
            pendingMoves,
            pendingChanges)

    private val activeAnimationsMapList = listOf(
            activeRemoves,
            activeDisappearUnknownLastPosition,
            activeDisappearKnownLastPosition,
            activeAppearKnownFirstPosition,
            activeAdds,
            activeMoves,
            activeChanges)

    override fun recordPreLayoutInformation(
            state: RecyclerView.State,
            viewHolder: RecyclerView.ViewHolder,
            changeFlags: Int,
            payloads: MutableList<Any>
    ): ItemHolderInfo {
        val taxiListItemHolderInfo = super.recordPreLayoutInformation(state, viewHolder, changeFlags, payloads) as TaxiListItemHolderInfo

        // TODO this must be here because there is a bug in ItemHolderInfo.setFrom method which sets
        // flags to 0 and not to real value
        taxiListItemHolderInfo.changeFlags = changeFlags

        return taxiListItemHolderInfo
                .apply {
                    this.payloads.addAll(payloads)
                }
    }

    override fun animateDisappearance(
            viewHolder: RecyclerView.ViewHolder,
            preLayoutInfo: ItemHolderInfo,
            postLayoutInfo: ItemHolderInfo?
    ): Boolean {
        endAnimation(viewHolder)

        return if (preLayoutInfo.isRemoved) {
            if (postLayoutInfo != null) throw IllegalStateException("Should this be possible?")

            processItemAnimation(
                    viewHolder,
                    ItemTranslateToRightAndFadeOutAnimation(viewHolder),
                    pendingRemoves)
        } else {
            if (postLayoutInfo == null) {
                processItemAnimation(
                        viewHolder,
                        ItemFadeOutAndScaleOutAnimation(viewHolder),
                        pendingDisappearUnknownLastPosition)
            } else {
                processItemAnimation(
                        viewHolder,
                        ItemMoveAndFadeAnimation(
                                viewHolder,
                                preLayoutInfo.left - postLayoutInfo.left,
                                0,
                                preLayoutInfo.top - postLayoutInfo.top,
                                0,
                                1f,
                                1f),
                        pendingDisappearKnownLastPosition)
            }
        }
    }

    override fun animateAppearance(
            viewHolder: RecyclerView.ViewHolder,
            preLayoutInfo: ItemHolderInfo?,
            postLayoutInfo: ItemHolderInfo
    ): Boolean {
        endAnimation(viewHolder)

        return if (preLayoutInfo == null) {
            processItemAnimation(
                    viewHolder,
                    ItemFadeInFromLeftAnimation(viewHolder),
                    pendingAdds)
        } else {
            processItemAnimation(
                    viewHolder,
                    ItemMoveAndFadeAnimation(
                            viewHolder,
                            preLayoutInfo.left - postLayoutInfo.left,
                            0,
                            preLayoutInfo.top - postLayoutInfo.top,
                            0,
                            1f,
                            1f),
                    pendingAppearKnownFirstPosition)
        }
    }

    override fun animatePersistence(
            viewHolder: RecyclerView.ViewHolder,
            preLayoutInfo: ItemHolderInfo,
            postLayoutInfo: ItemHolderInfo
    ): Boolean {
        endAnimation(viewHolder)

        return processItemAnimation(
                viewHolder,
                ItemMoveAndFadeAnimation(
                        viewHolder,
                        preLayoutInfo.left - postLayoutInfo.left,
                        0,
                        preLayoutInfo.top - postLayoutInfo.top,
                        0,
                        1f,
                        1f),
                pendingMoves)
    }

    override fun animateChange(
            oldHolder: RecyclerView.ViewHolder,
            newHolder: RecyclerView.ViewHolder,
            preLayoutInfo: ItemHolderInfo,
            postLayoutInfo: ItemHolderInfo
    ): Boolean {
        endAnimation(oldHolder)
        endAnimation(newHolder)

        val deltaX = postLayoutInfo.left - preLayoutInfo.left
        val deltaY = postLayoutInfo.top - preLayoutInfo.top

        val oldHolderRunPendingAnimationsRequested = processItemAnimation(
                oldHolder,
                ItemMoveAndFadeAnimation(oldHolder, 0, deltaX, 0, deltaY, 1f, 0f),
                pendingChanges)

        val newHolderRunPendingAnimationsRequested = processItemAnimation(
                newHolder,
                ItemMoveAndFadeAnimation(newHolder, -deltaX, 0, -deltaY, 0, 0f, 1f),
                pendingChanges)

        return oldHolderRunPendingAnimationsRequested || newHolderRunPendingAnimationsRequested
    }

    override fun runPendingAnimations() {
        val removeAnimators = startPendingAnimations(pendingRemoves, activeRemoves)
        val disappearUnknownLastPositionAnimators = startPendingAnimations(pendingDisappearUnknownLastPosition, activeDisappearUnknownLastPosition)
        val disappearKnownLastPositionAnimators = startPendingAnimations(pendingDisappearKnownLastPosition, activeDisappearKnownLastPosition)
        val appearKnownFirstPositionAnimators = startPendingAnimations(pendingAppearKnownFirstPosition, activeAppearKnownFirstPosition)
        val addAnimators = startPendingAnimations(pendingAdds, activeAdds)
        val moveAnimators = startPendingAnimations(pendingMoves, activeMoves)
        val changeAnimators = startPendingAnimations(pendingChanges, activeChanges)

        val removeAnimatorSet = AnimatorSet().apply {
            playTogether(removeAnimators)
        }
        val disappearUnknownLastPositionAnimatorSet = AnimatorSet().apply {
            playTogether(disappearUnknownLastPositionAnimators)
        }
        val disappearKnownLastPositionAnimatorSet = AnimatorSet().apply {
            playTogether(disappearKnownLastPositionAnimators)
        }
        val appearKnownFirstPositionAnimatorSet = AnimatorSet().apply {
            playTogether(appearKnownFirstPositionAnimators)
        }
        val addAnimatorSet = AnimatorSet().apply {
            playTogether(addAnimators)
        }
        val moveAnimatorSet = AnimatorSet().apply {
            playTogether(moveAnimators)
        }
        val changeAnimatorSet = AnimatorSet().apply {
            playTogether(changeAnimators)
        }

        val movingStuffAnimatorSet = AnimatorSet().apply {
            playTogether(disappearUnknownLastPositionAnimatorSet,
                    disappearKnownLastPositionAnimatorSet,
                    appearKnownFirstPositionAnimatorSet,
                    moveAnimatorSet,
                    changeAnimatorSet)
        }

        AnimatorSet().apply {
            playSequentially(
                    removeAnimatorSet,
                    movingStuffAnimatorSet,
                    addAnimatorSet)
            start()
        }
    }

    override fun endAnimation(item: RecyclerView.ViewHolder) {
        pendingAnimationsMapList.forEach {
            endViewHolderPendingAnimationIfAvailable(it, item)
        }

        activeAnimationsMapList.forEach {
            endViewHolderActiveAnimationIfAvailable(it, item)
        }
    }

    override fun endAnimations() {
        pendingAnimationsMapList.forEach {
            endAllPendingAnimations(it)
        }
        activeAnimationsMapList.forEach {
            endAllActiveAnimations(it)
        }
    }

    override fun isRunning(): Boolean {
        return pendingAnimationsMapList.any { it.isNotEmpty() }
                || activeAnimationsMapList.any { it.isNotEmpty() }
    }

    override fun canReuseUpdatedViewHolder(viewHolder: RecyclerView.ViewHolder, payloads: MutableList<Any>) =
            false // TODO payloads.isNotEmpty()

    override fun obtainHolderInfo(): ItemHolderInfo {
        return TaxiListItemHolderInfo()
    }
}

private fun RecyclerView.ItemAnimator.dispatchAnimationsFinishedIfNoneIsRunning() {
    if (!isRunning) {
        this.dispatchAnimationsFinished()
    }
}

private fun RecyclerView.ItemAnimator.processItemAnimation(
        holder: RecyclerView.ViewHolder,
        itemAnimation: ItemAnimation,
        pendingAnimationsMap: MutableMap<RecyclerView.ViewHolder, ItemAnimation>
): Boolean {
    return if (itemAnimation.shouldAnimate()) {
        itemAnimation.setupAnimator()
        itemAnimation.setStartingState()
        pendingAnimationsMap[holder] = itemAnimation
        true
    } else {
        this.dispatchAnimationFinished(holder)
        this.dispatchAnimationsFinishedIfNoneIsRunning()
        false
    }
}

private fun RecyclerView.ItemAnimator.startPendingAnimations(
        pendingAnimations: MutableMap<RecyclerView.ViewHolder, ItemAnimation>,
        activeAnimations: MutableMap<RecyclerView.ViewHolder, ItemAnimation>
): List<Animator> {
    val animators = pendingAnimations.map { (holder, itemAnimation) ->
        activeAnimations[holder] = itemAnimation

        itemAnimation.animator.apply {
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    activeAnimations.remove(holder)

                    itemAnimation.resetState()
                    dispatchAnimationFinished(holder)
                    dispatchAnimationsFinishedIfNoneIsRunning()
                }
            })
        }
    }
    pendingAnimations.clear()

    return animators
}

private fun RecyclerView.ItemAnimator.endViewHolderPendingAnimationIfAvailable(
        pendingAnimations: MutableMap<RecyclerView.ViewHolder, ItemAnimation>,
        holder: RecyclerView.ViewHolder) {
    pendingAnimations.remove(holder)?.let {
        it.resetState()
        this.dispatchAnimationFinished(holder)
        this.dispatchAnimationsFinishedIfNoneIsRunning()
    }
}

private fun endViewHolderActiveAnimationIfAvailable(
        activeAnimations: MutableMap<RecyclerView.ViewHolder, ItemAnimation>,
        holder: RecyclerView.ViewHolder) {
    activeAnimations.remove(holder)?.animator?.end()
}

private fun RecyclerView.ItemAnimator.endAllPendingAnimations(
        pendingAnimations: MutableMap<RecyclerView.ViewHolder, ItemAnimation>
) {
    pendingAnimations.keys.toList().forEach {
        this.dispatchAnimationFinished(it)
    }
    pendingAnimations.clear()
    dispatchAnimationsFinishedIfNoneIsRunning()
}

private fun endAllActiveAnimations(
        activeAnimations: MutableMap<RecyclerView.ViewHolder, ItemAnimation>
) {
    activeAnimations.values.toList().forEach {
        it.animator.end()
    }
    activeAnimations.clear()
}