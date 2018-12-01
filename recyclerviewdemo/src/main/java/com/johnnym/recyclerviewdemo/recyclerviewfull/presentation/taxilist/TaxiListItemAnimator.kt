package com.johnnym.recyclerviewdemo.recyclerviewfull.presentation.taxilist

import android.animation.*
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
            payloads: List<Any>
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
        val deltaX = postLayoutInfo.left - preLayoutInfo.left
        val deltaY = postLayoutInfo.top - preLayoutInfo.top

        if (oldHolder is NormalItemViewHolder
                && newHolder is NormalItemViewHolder
                && oldHolder == newHolder) {
            endAnimation(newHolder)

            val itemPayload = createCombinedTaxiListItemPayload((preLayoutInfo as TaxiListItemHolderInfo).payloads)

            return processItemAnimation(
                    newHolder,
                    ItemMoveAndTaxiStatusChangeAnimation(
                            newHolder,
                            -deltaX, 0,
                            -deltaY, 0,
                            itemPayload.taxiStatusChange),
                    pendingChanges)
        } else {
            if (oldHolder == newHolder) {
                endAnimation(newHolder)
                return processItemAnimation(
                        newHolder,
                        ItemMoveAndFadeAnimation(newHolder, -deltaX, 0, -deltaY, 0, 0f, 1f),
                        pendingChanges)
            } else {
                endAnimation(oldHolder)
                endAnimation(newHolder)

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
        }
    }

    override fun runPendingAnimations() {
        val removeAnimators = startPendingAnimations(pendingRemoves, activeRemoves)
        val disappearUnknownLastPositionAnimators = startPendingAnimations(pendingDisappearUnknownLastPosition, activeDisappearUnknownLastPosition)
        val disappearKnownLastPositionAnimators = startPendingAnimations(pendingDisappearKnownLastPosition, activeDisappearKnownLastPosition)
        val appearKnownFirstPositionAnimators = startPendingAnimations(pendingAppearKnownFirstPosition, activeAppearKnownFirstPosition)
        val addAnimators = startPendingAnimations(pendingAdds, activeAdds)
        val moveAnimators = startPendingAnimations(pendingMoves, activeMoves)
        val changeAnimators = startPendingAnimations(pendingChanges, activeChanges)

        removeAnimators.forEach {
            it.start()
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

        addAnimators.forEach {
            it.startDelay = 1000
            it.start()
        }
    }

    override fun endAnimation(item: RecyclerView.ViewHolder) {
        pendingAnimationsMapList.forEach { pendingAnimations ->
            pendingAnimations.remove(item)?.let {
                it.resetState()
                this.dispatchAnimationFinished(item)
                this.dispatchAnimationsFinishedIfNoneIsRunning()
            }
        }

        activeAnimationsMapList.forEach { activeAnimations ->
            activeAnimations.remove(item)?.let {
                if (it.animator.isStarted) {
                    it.animator.end()
                } else {
                    it.resetState()
                    this.dispatchAnimationFinished(item)
                    this.dispatchAnimationsFinishedIfNoneIsRunning()
                }
            }
        }
    }

    override fun endAnimations() {
        pendingAnimationsMapList.forEach { pendingAnimations ->
            pendingAnimations.entries.forEach {
                it.value.resetState()
                this.dispatchAnimationFinished(it.key)
                this.dispatchAnimationsFinishedIfNoneIsRunning()
            }
            pendingAnimations.clear()
        }
        activeAnimationsMapList.forEach { activeAnimations ->
            activeAnimations.entries.forEach {
                if (it.value.animator.isStarted) {
                    it.value.animator.end()
                } else {
                    it.value.resetState()
                    this.dispatchAnimationFinished(it.key)
                    this.dispatchAnimationsFinishedIfNoneIsRunning()
                }
            }
            activeAnimations.clear()
        }
    }

    override fun isRunning(): Boolean {
        return pendingAnimationsMapList.any { it.isNotEmpty() }
                || activeAnimationsMapList.any { it.isNotEmpty() }
    }

    override fun canReuseUpdatedViewHolder(viewHolder: RecyclerView.ViewHolder, payloads: MutableList<Any>) =
            payloads.isNotEmpty()

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
