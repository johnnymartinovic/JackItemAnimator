package com.johnnym.recyclerviewdemo.recyclerviewfull.presentation.taxilist

import android.animation.*
import android.animation.AnimatorSet
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber

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

        return taxiListItemHolderInfo
                .apply {
                    this.payloads.addAll(payloads)
                }
    }

    private fun logData(itemHolderInfo: ItemHolderInfo?): String {
        return if (itemHolderInfo == null) "null"
        else "%d %d %d %d".format(itemHolderInfo.left, itemHolderInfo.top, itemHolderInfo.right, itemHolderInfo.bottom)
    }

    private fun logData(viewHolder: RecyclerView.ViewHolder): String {
        return "%d %d %d %d %.2f %.2f Already animating: %s".format(
                viewHolder.itemView.left,
                viewHolder.itemView.top,
                viewHolder.itemView.right,
                viewHolder.itemView.bottom,
                viewHolder.itemView.translationX,
                viewHolder.itemView.translationY,
                viewHolder.alreadyAnimatingNames())
    }

    private fun RecyclerView.ViewHolder.alreadyAnimatingNames(): String {
        var output = ""

        if (pendingRemoves.contains(this)) output += "pendingRemoves "
        if (pendingDisappearUnknownLastPosition.contains(this)) output += "pendingDisappearUnknownLastPosition "
        if (pendingDisappearKnownLastPosition.contains(this)) output += "pendingDisappearKnownLastPosition "
        if (pendingAppearKnownFirstPosition.contains(this)) output += "pendingAppearKnownFirstPosition "
        if (pendingAdds.contains(this)) output += "pendingAdds "
        if (pendingMoves.contains(this)) output += "pendingMoves "
        if (pendingChanges.contains(this)) output += "pendingChanges "
        if (activeRemoves.contains(this)) output += "activeRemoves "
        if (activeDisappearUnknownLastPosition.contains(this)) output += "activeDisappearUnknownLastPosition "
        if (activeDisappearKnownLastPosition.contains(this)) output += "activeDisappearKnownLastPosition "
        if (activeAppearKnownFirstPosition.contains(this)) output += "activeAppearKnownFirstPosition "
        if (activeAdds.contains(this)) output += "activeAdds "
        if (activeMoves.contains(this)) output += "activeMoves "
        if (activeChanges.contains(this)) output += "activeChanges "

        return output
    }

    override fun animateDisappearance(
            viewHolder: RecyclerView.ViewHolder,
            preLayoutInfo: ItemHolderInfo,
            postLayoutInfo: ItemHolderInfo?
    ): Boolean {
        cancelAnimation(viewHolder)

        Timber.d("""
            ----------------
            Disappearance
            PreLayoutInfo:  ${logData(preLayoutInfo)}
            ViewHolder:     ${logData(viewHolder)}
            PostLayoutInfo: ${logData(postLayoutInfo)}
        """.trimIndent())

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

        Timber.d("""
            ----------------
            Appearance
            PreLayoutInfo:  ${logData(preLayoutInfo)}
            ViewHolder:     ${logData(viewHolder)}
            PostLayoutInfo: ${logData(postLayoutInfo)}
        """.trimIndent())

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

        Timber.d("""
            ----------------
            Persistence
            PreLayoutInfo: ${logData(preLayoutInfo)}
            ViewHolder: ${logData(viewHolder)}
            PostLayoutInfo: ${logData(postLayoutInfo)}
        """.trimIndent())

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

        Timber.d("""
            ----------------
            Change
            PreLayoutInfo:  ${logData(preLayoutInfo)}
            ViewHolder:     ${logData(oldHolder)}
            PostLayoutInfo: ${logData(postLayoutInfo)}
            ViewHolder:     ${logData(newHolder)}
        """.trimIndent())

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

    private fun cancelAnimation(item: RecyclerView.ViewHolder) {
        pendingAnimationsMapList.forEach {
            cancelViewHolderPendingAnimationIfAvailable(it, item)
        }

        activeAnimationsMapList.forEach {
            cancelViewHolderActiveAnimationIfAvailable(it, item)
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

    private fun resetAnimation() {

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
    return if (itemAnimation.setup()) {
        pendingAnimationsMap[holder] = itemAnimation
        return true
    } else {
        this.dispatchAnimationFinished(holder)
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
                    dispatchAnimationFinished(holder)
                    activeAnimations.remove(holder)
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
    }
}

private fun RecyclerView.ItemAnimator.cancelViewHolderPendingAnimationIfAvailable(
        pendingAnimations: MutableMap<RecyclerView.ViewHolder, ItemAnimation>,
        holder: RecyclerView.ViewHolder) {
    pendingAnimations.remove(holder)?.let {
        this.dispatchAnimationFinished(holder)
    }
}

private fun endViewHolderActiveAnimationIfAvailable(
        activeAnimations: MutableMap<RecyclerView.ViewHolder, ItemAnimation>,
        holder: RecyclerView.ViewHolder) {
    activeAnimations.remove(holder)?.let {
        it.resetState()
        it.animator.end()
    }
}

private fun cancelViewHolderActiveAnimationIfAvailable(
        activeAnimations: MutableMap<RecyclerView.ViewHolder, ItemAnimation>,
        holder: RecyclerView.ViewHolder) {
    activeAnimations.remove(holder)?.animator?.cancel()
}

private fun RecyclerView.ItemAnimator.endAllPendingAnimations(
        pendingAnimations: MutableMap<RecyclerView.ViewHolder, ItemAnimation>) {
    pendingAnimations.keys.toList().forEach {
        this.dispatchAnimationFinished(it)
    }
    pendingAnimations.clear()
}

private fun endAllActiveAnimations(activeAnimations: MutableMap<RecyclerView.ViewHolder, ItemAnimation>) {
    activeAnimations.values.toList().forEach {
        it.animator.end()
    }
    activeAnimations.clear()
}