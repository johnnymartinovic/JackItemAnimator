package com.johnnym.recyclerviewdemo.recyclerviewfull.presentation.taxilist

import android.animation.*
import android.support.v7.widget.RecyclerView
import android.animation.AnimatorSet
import android.util.Log

class TaxiListItemAnimator : RecyclerView.ItemAnimator() {

    companion object {
        private const val LOG_TAG = "ANIMATOR"
    }

    private val pendingRemoves: MutableMap<RecyclerView.ViewHolder, Animator> = HashMap()
    private val pendingDisappearUnknownLastPosition: MutableMap<RecyclerView.ViewHolder, Animator> = HashMap()
    private val pendingDisappearKnownLastPosition: MutableMap<RecyclerView.ViewHolder, Animator> = HashMap()
    private val pendingAppearKnownFirstPosition: MutableMap<RecyclerView.ViewHolder, Animator> = HashMap()
    private val pendingAdds: MutableMap<RecyclerView.ViewHolder, Animator> = HashMap()
    private val pendingMoves: MutableMap<RecyclerView.ViewHolder, Animator> = HashMap()
    private val pendingChanges: MutableMap<RecyclerView.ViewHolder, Animator> = HashMap()

    private val activeRemoves: MutableMap<RecyclerView.ViewHolder, Animator> = HashMap()
    private val activeDisappearUnknownLastPosition: MutableMap<RecyclerView.ViewHolder, Animator> = HashMap()
    private val activeDisappearKnownLastPosition: MutableMap<RecyclerView.ViewHolder, Animator> = HashMap()
    private val activeAppearKnownFirstPosition: MutableMap<RecyclerView.ViewHolder, Animator> = HashMap()
    private val activeAdds: MutableMap<RecyclerView.ViewHolder, Animator> = HashMap()
    private val activeMoves: MutableMap<RecyclerView.ViewHolder, Animator> = HashMap()
    private val activeChanges: MutableMap<RecyclerView.ViewHolder, Animator> = HashMap()

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
        return "%d %d %d %d".format(viewHolder.itemView.left, viewHolder.itemView.top, viewHolder.itemView.right, viewHolder.itemView.bottom)
    }

    override fun animateDisappearance(
            viewHolder: RecyclerView.ViewHolder,
            preLayoutInfo: ItemHolderInfo,
            postLayoutInfo: ItemHolderInfo?
    ): Boolean {
        Log.d(LOG_TAG, """
            ----------------
            Disappearance
            PreLayoutInfo:  ${logData(preLayoutInfo)}
            ViewHolder:     ${logData(viewHolder)}
            PostLayoutInfo: ${logData(postLayoutInfo)}
        """.trimIndent())

        return if (preLayoutInfo.isRemoved) {
            if (postLayoutInfo != null) throw IllegalStateException("Should this be possible?")

            processItemAnimation(
                    ItemTranslateToRightAndFadeOutAnimation(viewHolder),
                    pendingRemoves)
        } else {
            if (postLayoutInfo == null) {
                processItemAnimation(
                        ItemFadeOutAndScaleOutAnimation(viewHolder),
                        pendingDisappearUnknownLastPosition)
            } else {
                processItemAnimation(
                        ItemMoveAndFadeAnimation(
                                viewHolder,
                                preLayoutInfo.left, preLayoutInfo.top, 1f,
                                postLayoutInfo.left, postLayoutInfo.top, 1f),
                        pendingDisappearKnownLastPosition)
            }
        }
    }

    override fun animateAppearance(
            viewHolder: RecyclerView.ViewHolder,
            preLayoutInfo: ItemHolderInfo?,
            postLayoutInfo: ItemHolderInfo
    ): Boolean {
        Log.d(LOG_TAG, """
            ----------------
            Appearance
            PreLayoutInfo:  ${logData(preLayoutInfo)}
            ViewHolder:     ${logData(viewHolder)}
            PostLayoutInfo: ${logData(postLayoutInfo)}
        """.trimIndent())

        return if (preLayoutInfo == null) {
            processItemAnimation(
                    ItemFadeInFromLeftAnimation(viewHolder),
                    pendingAdds)
        } else {
            processItemAnimation(
                    ItemMoveAndFadeAnimation(
                            viewHolder,
                            preLayoutInfo.left, preLayoutInfo.top, 1f,
                            postLayoutInfo.left, postLayoutInfo.top, 1f),
                    pendingAppearKnownFirstPosition)
        }
    }

    override fun animatePersistence(
            viewHolder: RecyclerView.ViewHolder,
            preLayoutInfo: ItemHolderInfo,
            postLayoutInfo: ItemHolderInfo
    ): Boolean {
        Log.d(LOG_TAG, """
            ----------------
            Persistence
            PreLayoutInfo: ${logData(preLayoutInfo)}
            ViewHolder: ${logData(viewHolder)}
            PostLayoutInfo: ${logData(postLayoutInfo)}
        """.trimIndent())

        return processItemAnimation(
                ItemMoveAndFadeAnimation(
                        viewHolder,
                        preLayoutInfo.left, preLayoutInfo.top, 1f,
                        postLayoutInfo.left, postLayoutInfo.top, 1f),
                pendingMoves)
    }

    override fun animateChange(
            oldHolder: RecyclerView.ViewHolder,
            newHolder: RecyclerView.ViewHolder,
            preLayoutInfo: ItemHolderInfo,
            postLayoutInfo: ItemHolderInfo
    ): Boolean {
        Log.d(LOG_TAG, """
            ----------------
            Change
            PreLayoutInfo:  ${logData(preLayoutInfo)}
            ViewHolder:     ${logData(oldHolder)}
            PostLayoutInfo: ${logData(postLayoutInfo)}
            ViewHolder:     ${logData(newHolder)}
        """.trimIndent())

        val fromX = preLayoutInfo.left
        val fromY = preLayoutInfo.top
        val toX = postLayoutInfo.left
        val toY = postLayoutInfo.top

        val oldHolderRunPendingAnimationsRequested = processItemAnimation(
                ItemMoveAndFadeAnimation(oldHolder, fromX, fromY, 1f, toX, toY, 0f),
                pendingChanges)

        val newHolderRunPendingAnimationsRequested = processItemAnimation(
                ItemMoveAndFadeAnimation(newHolder, fromX, fromY, 0f, toX, toY, 1f),
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
        dispatchAnimationsFinished()
    }
}

private fun RecyclerView.ItemAnimator.processItemAnimation(
        itemAnimation: ItemAnimation,
        pendingAnimationsMap: MutableMap<RecyclerView.ViewHolder, Animator>
): Boolean {
    val animator = itemAnimation.setupAndGetAnimator()
    val holder = itemAnimation.holder

    return if (animator == null) {
        dispatchAnimationFinished(holder)
        false
    } else {
        pendingAnimationsMap[holder] = animator
        return true
    }
}

private fun RecyclerView.ItemAnimator.startPendingAnimations(
        pendingAnimations: MutableMap<RecyclerView.ViewHolder, Animator>,
        activeAnimations: MutableMap<RecyclerView.ViewHolder, Animator>
): List<Animator> {
    val animators = pendingAnimations.map { (holder, animator) ->
        activeAnimations[holder] = animator

        animator.apply {
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
        pendingAnimations: MutableMap<RecyclerView.ViewHolder, Animator>,
        holder: RecyclerView.ViewHolder) {
    if (pendingAnimations.remove(holder) != null) {
        dispatchAnimationFinished(holder)
    }
}

private fun endViewHolderActiveAnimationIfAvailable(
        activeAnimations: MutableMap<RecyclerView.ViewHolder, Animator>,
        holder: RecyclerView.ViewHolder) {
    activeAnimations.remove(holder)?.end()
}

private fun RecyclerView.ItemAnimator.endAllPendingAnimations(
        pendingAnimations: MutableMap<RecyclerView.ViewHolder, Animator>) {
    pendingAnimations.keys.toList().forEach {
        dispatchAnimationFinished(it)
    }
    pendingAnimations.clear()
}

private fun endAllActiveAnimations(activeAnimations: MutableMap<RecyclerView.ViewHolder, Animator>) {
    activeAnimations.values.toList().forEach {
        it.end()
    }
    activeAnimations.clear()
}