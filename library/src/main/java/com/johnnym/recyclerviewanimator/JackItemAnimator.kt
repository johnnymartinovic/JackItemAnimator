package com.johnnym.recyclerviewanimator

import android.animation.*
import androidx.recyclerview.widget.RecyclerView

// TODO check in which animate methods payloads exists in itemholderinfo
abstract class JackItemAnimator : RecyclerView.ItemAnimator() {

    private val pendingRemoves: MutableMap<RecyclerView.ViewHolder, JackItemAnimationHandler> = HashMap()
    private val pendingDisappearUnknownLastPosition: MutableMap<RecyclerView.ViewHolder, JackItemAnimationHandler> = HashMap()
    private val pendingDisappearKnownLastPosition: MutableMap<RecyclerView.ViewHolder, JackItemAnimationHandler> = HashMap()
    private val pendingAdds: MutableMap<RecyclerView.ViewHolder, JackItemAnimationHandler> = HashMap()
    private val pendingAppearKnownFirstPosition: MutableMap<RecyclerView.ViewHolder, JackItemAnimationHandler> = HashMap()
    private val pendingMoves: MutableMap<RecyclerView.ViewHolder, JackItemAnimationHandler> = HashMap()
    private val pendingChanges: MutableMap<RecyclerView.ViewHolder, JackItemAnimationHandler> = HashMap()

    private val activeRemoves: MutableMap<RecyclerView.ViewHolder, JackItemAnimationHandler> = HashMap()
    private val activeDisappearUnknownLastPosition: MutableMap<RecyclerView.ViewHolder, JackItemAnimationHandler> = HashMap()
    private val activeDisappearKnownLastPosition: MutableMap<RecyclerView.ViewHolder, JackItemAnimationHandler> = HashMap()
    private val activeAdds: MutableMap<RecyclerView.ViewHolder, JackItemAnimationHandler> = HashMap()
    private val activeAppearKnownFirstPosition: MutableMap<RecyclerView.ViewHolder, JackItemAnimationHandler> = HashMap()
    private val activeMoves: MutableMap<RecyclerView.ViewHolder, JackItemAnimationHandler> = HashMap()
    private val activeChanges: MutableMap<RecyclerView.ViewHolder, JackItemAnimationHandler> = HashMap()

    private val pendingAnimationsMapList = listOf(
            pendingRemoves,
            pendingDisappearUnknownLastPosition,
            pendingDisappearKnownLastPosition,
            pendingAdds,
            pendingAppearKnownFirstPosition,
            pendingMoves,
            pendingChanges)

    private val activeAnimationsMapList = listOf(
            activeRemoves,
            activeDisappearUnknownLastPosition,
            activeDisappearKnownLastPosition,
            activeAdds,
            activeAppearKnownFirstPosition,
            activeMoves,
            activeChanges)

    override fun recordPreLayoutInformation(
            state: RecyclerView.State,
            viewHolder: RecyclerView.ViewHolder,
            changeFlags: Int,
            payloads: List<Any>
    ): ItemHolderInfo {
        val jackItemHolderInfo = super.recordPreLayoutInformation(state, viewHolder, changeFlags, payloads) as JackItemHolderInfo

        return jackItemHolderInfo.apply {
            // TODO changeFlags must be updated here because there is a bug in
            // ItemHolderInfo.setFrom(...) method which sets flags to 0 and not to real value.
            // Check the status of submitted bug report:
            // https://issuetracker.google.com/issues/112761172
            this.changeFlags = changeFlags
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
            if (postLayoutInfo != null) throw IllegalStateException("This should be possible?")

            processJackItemAnimation(
                    viewHolder,
                    createRemoval(viewHolder),
                    pendingRemoves)
        } else {
            if (postLayoutInfo == null) {
                processJackItemAnimation(
                        viewHolder,
                        createDisappearUnknownLastPosition(viewHolder),
                        pendingDisappearUnknownLastPosition)
            } else {
                processJackItemAnimation(
                        viewHolder,
                        createDisappearKnownLastPosition(
                                viewHolder,
                                preLayoutInfo.left - postLayoutInfo.left,
                                preLayoutInfo.top - postLayoutInfo.top),
                        pendingDisappearKnownLastPosition)
            }
        }
    }

    abstract fun createRemoval(
            viewHolder: RecyclerView.ViewHolder
    ): JackItemAnimation

    abstract fun createDisappearUnknownLastPosition(
            viewHolder: RecyclerView.ViewHolder
    ): JackItemAnimation

    abstract fun createDisappearKnownLastPosition(
            viewHolder: RecyclerView.ViewHolder,
            deltaX: Int,
            deltaY: Int
    ): JackItemAnimation

    override fun animateAppearance(
            viewHolder: RecyclerView.ViewHolder,
            preLayoutInfo: ItemHolderInfo?,
            postLayoutInfo: ItemHolderInfo
    ): Boolean {
        endAnimation(viewHolder)

        return if (preLayoutInfo == null) {
            processJackItemAnimation(
                    viewHolder,
                    createAdd(viewHolder),
                    pendingAdds)
        } else {
            processJackItemAnimation(
                    viewHolder,
                    createAppearKnownFirstPosition(
                            viewHolder,
                            preLayoutInfo.left - postLayoutInfo.left,
                            preLayoutInfo.top - postLayoutInfo.top),
                    pendingAppearKnownFirstPosition)
        }
    }

    abstract fun createAdd(
            viewHolder: RecyclerView.ViewHolder
    ): JackItemAnimation

    abstract fun createAppearKnownFirstPosition(
            viewHolder: RecyclerView.ViewHolder,
            deltaX: Int,
            deltaY: Int
    ): JackItemAnimation

    override fun animatePersistence(
            viewHolder: RecyclerView.ViewHolder,
            preLayoutInfo: ItemHolderInfo,
            postLayoutInfo: ItemHolderInfo
    ): Boolean {
        endAnimation(viewHolder)

        return processJackItemAnimation(
                viewHolder,
                createMove(
                        viewHolder,
                        preLayoutInfo.left - postLayoutInfo.left,
                        preLayoutInfo.top - postLayoutInfo.top),
                pendingMoves)
    }

    abstract fun createMove(
            viewHolder: RecyclerView.ViewHolder,
            deltaX: Int,
            deltaY: Int
    ): JackItemAnimation

    override fun animateChange(
            oldHolder: RecyclerView.ViewHolder,
            newHolder: RecyclerView.ViewHolder,
            preLayoutInfo: ItemHolderInfo,
            postLayoutInfo: ItemHolderInfo
    ): Boolean {
        val deltaX = preLayoutInfo.left - postLayoutInfo.left
        val deltaY = preLayoutInfo.top - postLayoutInfo.top

        if (oldHolder == newHolder) {
            endAnimation(newHolder)

            val payloads = (preLayoutInfo as JackItemHolderInfo).payloads

            return processJackItemAnimation(
                    newHolder,
                    createSameHolderItemChange(
                            newHolder,
                            deltaX,
                            deltaY,
                            payloads),
                    pendingChanges)
        } else {
            endAnimation(oldHolder)
            endAnimation(newHolder)

            val jackItemAnimationPair = createDifferentHolderItemChange(
                    oldHolder,
                    newHolder,
                    deltaX,
                    deltaY)

            val oldHolderRunPendingAnimationsRequested = processJackItemAnimation(
                    oldHolder,
                    jackItemAnimationPair.first,
                    pendingChanges)

            val newHolderRunPendingAnimationsRequested = processJackItemAnimation(
                    newHolder,
                    jackItemAnimationPair.second,
                    pendingChanges)

            return oldHolderRunPendingAnimationsRequested || newHolderRunPendingAnimationsRequested
        }
    }

    abstract fun createSameHolderItemChange(
            viewHolder: RecyclerView.ViewHolder,
            deltaX: Int,
            deltaY: Int,
            payloads: List<Any>
    ): JackItemAnimation

    abstract fun createDifferentHolderItemChange(
            oldHolder: RecyclerView.ViewHolder,
            newHolder: RecyclerView.ViewHolder,
            deltaX: Int,
            deltaY: Int
    ): Pair<JackItemAnimation, JackItemAnimation>

    override fun runPendingAnimations() {
        val removeAnimators = moveAnimationsFromPendingToActive(pendingRemoves, activeRemoves)
        val disappearUnknownLastPositionAnimators = moveAnimationsFromPendingToActive(pendingDisappearUnknownLastPosition, activeDisappearUnknownLastPosition)
        val disappearKnownLastPositionAnimators = moveAnimationsFromPendingToActive(pendingDisappearKnownLastPosition, activeDisappearKnownLastPosition)
        val appearKnownFirstPositionAnimators = moveAnimationsFromPendingToActive(pendingAppearKnownFirstPosition, activeAppearKnownFirstPosition)
        val addAnimators = moveAnimationsFromPendingToActive(pendingAdds, activeAdds)
        val moveAnimators = moveAnimationsFromPendingToActive(pendingMoves, activeMoves)
        val changeAnimators = moveAnimationsFromPendingToActive(pendingChanges, activeChanges)

        handleAnimations(
                removeAnimators,
                disappearUnknownLastPositionAnimators,
                disappearKnownLastPositionAnimators,
                appearKnownFirstPositionAnimators,
                addAnimators,
                moveAnimators,
                changeAnimators)
    }

    abstract fun handleAnimations(
            removeAnimators: List<Animator>,
            disappearUnknownLastPositionAnimators: List<Animator>,
            disappearKnownLastPositionAnimators: List<Animator>,
            appearKnownFirstPositionAnimators: List<Animator>,
            addAnimators: List<Animator>,
            moveAnimators: List<Animator>,
            changeAnimators: List<Animator>)

    override fun endAnimation(item: RecyclerView.ViewHolder) {
        pendingAnimationsMapList.forEach { pendingAnimations ->
            pendingAnimations.remove(item)?.let {
                it.jackItemAnimation.resetState()
                this.dispatchAnimationFinished(item)
                this.dispatchAnimationsFinishedIfNoneIsRunning()
            }
        }
        activeAnimationsMapList.forEach { activeAnimations ->
            activeAnimations.remove(item)?.let {
                if (it.animator.isStarted) {
                    it.animator.end()
                } else {
                    it.jackItemAnimation.resetState()
                    this.dispatchAnimationFinished(item)
                    this.dispatchAnimationsFinishedIfNoneIsRunning()
                }
            }
        }
    }

    override fun endAnimations() {
        pendingAnimationsMapList.forEach { pendingAnimations ->
            pendingAnimations.entries.forEach {
                it.value.jackItemAnimation.resetState()
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
                    it.value.jackItemAnimation.resetState()
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
        return JackItemHolderInfo()
    }
}

private fun RecyclerView.ItemAnimator.dispatchAnimationsFinishedIfNoneIsRunning() {
    if (!isRunning) {
        this.dispatchAnimationsFinished()
    }
}

private fun RecyclerView.ItemAnimator.processJackItemAnimation(
        holder: RecyclerView.ViewHolder,
        jackItemAnimation: JackItemAnimation,
        pendingAnimationsMap: MutableMap<RecyclerView.ViewHolder, JackItemAnimationHandler>
): Boolean {
    val jackItemAnimationHandler = JackItemAnimationHandler(jackItemAnimation)

    val shouldRequestToRunPendingAnimations =
            if (jackItemAnimationHandler.jackItemAnimation.willAnimate()) {
                jackItemAnimationHandler.jackItemAnimation.setStartingState()
                pendingAnimationsMap[holder] = jackItemAnimationHandler

                true
            } else {
                this.dispatchAnimationFinished(holder)
                this.dispatchAnimationsFinishedIfNoneIsRunning()

                false
            }

    return shouldRequestToRunPendingAnimations
}

private fun RecyclerView.ItemAnimator.moveAnimationsFromPendingToActive(
        pendingAnimations: MutableMap<RecyclerView.ViewHolder, JackItemAnimationHandler>,
        activeAnimations: MutableMap<RecyclerView.ViewHolder, JackItemAnimationHandler>
): List<Animator> {
    val animators = pendingAnimations.map { (holder, itemAnimation) ->
        activeAnimations[holder] = itemAnimation

        itemAnimation.animator.apply {
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    activeAnimations.remove(holder)

                    itemAnimation.jackItemAnimation.resetState()
                    dispatchAnimationFinished(holder)
                    dispatchAnimationsFinishedIfNoneIsRunning()
                }
            })
        }
    }
    pendingAnimations.clear()

    return animators
}
