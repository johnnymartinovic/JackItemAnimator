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
            // ItemHolderInfo.setFrom(...) method which sets flags to 0 and not to real value
            this.changeFlags = changeFlags
            this.payloads.addAll(payloads)
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

    abstract fun createAdd(
            viewHolder: RecyclerView.ViewHolder
    ): JackItemAnimation

    abstract fun createAppearKnownFirstPosition(
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

    abstract fun createMove(
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
    return if (jackItemAnimationHandler.jackItemAnimation.willAnimate()) {
        jackItemAnimationHandler.jackItemAnimation.setStartingState()
        pendingAnimationsMap[holder] = jackItemAnimationHandler
        true
    } else {
        this.dispatchAnimationFinished(holder)
        this.dispatchAnimationsFinishedIfNoneIsRunning()
        false
    }
}

private fun RecyclerView.ItemAnimator.startPendingAnimations(
        pendingAnimations: MutableMap<RecyclerView.ViewHolder, JackItemAnimationHandler>,
        activeAnimations: MutableMap<RecyclerView.ViewHolder, JackItemAnimationHandler>
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
