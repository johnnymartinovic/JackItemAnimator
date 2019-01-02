package com.johnnym.recyclerviewanimator

import androidx.recyclerview.widget.RecyclerView

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
            holder: RecyclerView.ViewHolder
    ): JackItemAnimation

    abstract fun createDisappearUnknownLastPosition(
            holder: RecyclerView.ViewHolder
    ): JackItemAnimation

    abstract fun createDisappearKnownLastPosition(
            holder: RecyclerView.ViewHolder,
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
            holder: RecyclerView.ViewHolder
    ): JackItemAnimation

    abstract fun createAppearKnownFirstPosition(
            holder: RecyclerView.ViewHolder,
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
            holder: RecyclerView.ViewHolder,
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

            val changeJackItemAnimations = createDifferentHolderItemChange(
                    oldHolder,
                    newHolder,
                    deltaX,
                    deltaY)

            val oldHolderRunPendingAnimationsRequested = processJackItemAnimation(
                    oldHolder,
                    changeJackItemAnimations.oldHolderAnimation,
                    pendingChanges)

            val newHolderRunPendingAnimationsRequested = processJackItemAnimation(
                    newHolder,
                    changeJackItemAnimations.newHolderAnimation,
                    pendingChanges)

            return oldHolderRunPendingAnimationsRequested || newHolderRunPendingAnimationsRequested
        }
    }

    abstract fun createSameHolderItemChange(
            holder: RecyclerView.ViewHolder,
            deltaX: Int,
            deltaY: Int,
            payloads: List<Any>
    ): JackItemAnimation

    abstract fun createDifferentHolderItemChange(
            oldHolder: RecyclerView.ViewHolder,
            newHolder: RecyclerView.ViewHolder,
            deltaX: Int,
            deltaY: Int
    ): ChangeJackItemAnimations

    override fun runPendingAnimations() {
        val removeHolderAnimators = moveAnimationsFromPendingToActive(pendingRemoves, activeRemoves)
        val disappearUnknownLastPositionHolderAnimators = moveAnimationsFromPendingToActive(pendingDisappearUnknownLastPosition, activeDisappearUnknownLastPosition)
        val disappearKnownLastPositionHolderAnimators = moveAnimationsFromPendingToActive(pendingDisappearKnownLastPosition, activeDisappearKnownLastPosition)
        val appearKnownFirstPositionHolderAnimators = moveAnimationsFromPendingToActive(pendingAppearKnownFirstPosition, activeAppearKnownFirstPosition)
        val addHolderAnimators = moveAnimationsFromPendingToActive(pendingAdds, activeAdds)
        val moveHolderAnimators = moveAnimationsFromPendingToActive(pendingMoves, activeMoves)
        val changeHolderAnimators = moveAnimationsFromPendingToActive(pendingChanges, activeChanges)

        handleAnimations(
                removeHolderAnimators,
                disappearUnknownLastPositionHolderAnimators,
                disappearKnownLastPositionHolderAnimators,
                appearKnownFirstPositionHolderAnimators,
                addHolderAnimators,
                moveHolderAnimators,
                changeHolderAnimators)
    }

    abstract fun handleAnimations(
            removeHolderAnimators: List<HolderAnimator>,
            disappearUnknownLastPositionHolderAnimators: List<HolderAnimator>,
            disappearKnownLastPositionHolderAnimators: List<HolderAnimator>,
            appearKnownFirstPositionHolderAnimators: List<HolderAnimator>,
            addHolderAnimators: List<HolderAnimator>,
            moveHolderAnimators: List<HolderAnimator>,
            changeHolderAnimators: List<HolderAnimator>)

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
                    it.value.animationEndCallback = null
                    it.value.animator.end()
                }

                it.value.jackItemAnimation.resetState()
                this.dispatchAnimationFinished(it.key)
                this.dispatchAnimationsFinishedIfNoneIsRunning()
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
): List<HolderAnimator> {
    val holderAnimators = pendingAnimations.map { (holder, itemAnimation) ->
        activeAnimations[holder] = itemAnimation

        itemAnimation.animationEndCallback = {
            activeAnimations.remove(holder)

            itemAnimation.jackItemAnimation.resetState()
            dispatchAnimationFinished(holder)
            dispatchAnimationsFinishedIfNoneIsRunning()
        }

        HolderAnimator(holder, itemAnimation.animator)
    }
    pendingAnimations.clear()

    return holderAnimators
}
