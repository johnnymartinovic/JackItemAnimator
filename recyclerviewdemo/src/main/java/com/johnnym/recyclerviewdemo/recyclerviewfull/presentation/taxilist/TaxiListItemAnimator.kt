package com.johnnym.recyclerviewdemo.recyclerviewfull.presentation.taxilist

import android.animation.*
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.RecyclerView
import android.animation.AnimatorSet

class TaxiListItemAnimator : DefaultItemAnimator() {

    private val pendingRemoves: MutableMap<RecyclerView.ViewHolder, Animator> = HashMap()
    private val pendingAdds: MutableMap<RecyclerView.ViewHolder, Animator> = HashMap()
    private val pendingMoves: MutableMap<RecyclerView.ViewHolder, Animator> = HashMap()
    private val pendingChanges: MutableMap<RecyclerView.ViewHolder, Animator> = HashMap()

    private val activeRemoves: MutableMap<RecyclerView.ViewHolder, Animator> = HashMap()
    private val activeAdds: MutableMap<RecyclerView.ViewHolder, Animator> = HashMap()
    private val activeMoves: MutableMap<RecyclerView.ViewHolder, Animator> = HashMap()
    private val activeChanges: MutableMap<RecyclerView.ViewHolder, Animator> = HashMap()

    override fun obtainHolderInfo(): ItemHolderInfo {
        return TaxiListItemHolderInfo()
    }

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

    override fun isRunning(): Boolean {
        return super.isRunning()
                || pendingRemoves.isNotEmpty()
                || pendingAdds.isNotEmpty()
                || pendingMoves.isNotEmpty()
                || pendingChanges.isNotEmpty()
                || activeRemoves.isNotEmpty()
                || activeAdds.isNotEmpty()
                || activeMoves.isNotEmpty()
                || activeChanges.isNotEmpty()
    }

    override fun endAnimation(item: RecyclerView.ViewHolder) {
        if (pendingRemoves.containsKey(item)) {
            pendingRemoves.remove(item)
            dispatchRemoveFinished(item)
        }
        if (pendingAdds.containsKey(item)) {
            pendingAdds.remove(item)
            dispatchAddFinished(item)
        }
        if (pendingMoves.containsKey(item)) {
            pendingMoves.remove(item)
            dispatchMoveFinished(item)
        }
        if (pendingChanges.containsKey(item)) {
            pendingChanges.remove(item)
            dispatchAnimationFinished(item)
        }

        activeRemoves.remove(item)?.end()

        activeAdds.remove(item)?.end()

        activeMoves.remove(item)?.end()

        activeChanges.remove(item)?.end()

        super.endAnimation(item)
    }

    override fun endAnimations() {
        pendingRemoves.keys.forEach {
            dispatchRemoveFinished(it)
        }
        pendingRemoves.clear()

        pendingAdds.keys.forEach {
            dispatchAddFinished(it)
        }
        pendingAdds.clear()

        pendingMoves.keys.forEach {
            dispatchMoveFinished(it)
        }
        pendingMoves.clear()

        pendingChanges.keys.forEach {
            dispatchAnimationFinished(it)
        }
        pendingChanges.clear()

        activeRemoves.values.forEach {
            it.end()
        }
        activeRemoves.clear()

        activeAdds.values.forEach {
            it.end()
        }
        activeAdds.clear()

        activeMoves.values.forEach {
            it.end()
        }
        activeMoves.clear()

        activeChanges.values.forEach {
            it.end()
        }
        activeChanges.clear()

        super.endAnimations()
    }

    internal fun dispatchAnimationsFinishedIfNoneIsRunning() {
        if (!isRunning) {
            dispatchAnimationsFinished()
        }
    }

    override fun runPendingAnimations() {
        super.runPendingAnimations()

        val removeAnimators = AnimatorSet().apply {
            playTogether(pendingRemoves.values)
        }
        val addAnimators = AnimatorSet().apply {
            playTogether(pendingAdds.values)
        }
        val moveAnimators = AnimatorSet().apply {
            playTogether(pendingMoves.values)
        }
        val changeAnimators = AnimatorSet().apply {
            playTogether(pendingChanges.values)
        }
        val moveChangeAnimators = AnimatorSet().apply {
            playTogether(moveAnimators, changeAnimators)
        }

        AnimatorSet().apply {
            playSequentially(removeAnimators, moveChangeAnimators, addAnimators)
            start()
        }
    }

    override fun animateRemove(holder: RecyclerView.ViewHolder): Boolean {
        val removeAnimator = when (holder) {
            is TaxiListAdapter.ItemViewHolder -> setupItemViewHolderAndCreateRemoveAnimator(holder)
            is TaxiListAdapter.SquareItemViewHolder -> setupSquareItemViewHolderAndCreateRemoveAnimator(holder)
            else -> return super.animateRemove(holder)
        }

        removeAnimator.addListener(object : AnimatorListenerAdapter() {

            override fun onAnimationStart(animation: Animator) {
                val animator = pendingRemoves.remove(holder)
                dispatchRemoveStarting(holder)
                if (animator != null) {
                    activeRemoves[holder] = animator
                }
            }

            override fun onAnimationEnd(animation: Animator) {
                dispatchRemoveFinished(holder)
                activeRemoves.remove(holder)
                dispatchAnimationsFinishedIfNoneIsRunning()
            }
        })

        pendingRemoves[holder] = removeAnimator

        return true
    }

    override fun animateAdd(holder: RecyclerView.ViewHolder): Boolean {
        val addAnimator = setupHolderAndCreateAddAnimator(holder)

        addAnimator.addListener(object : AnimatorListenerAdapter() {

            override fun onAnimationStart(animation: Animator) {
                val animator = pendingAdds.remove(holder)
                dispatchAddStarting(holder)
                if (animator != null) {
                    activeAdds[holder] = animator
                }
            }

            override fun onAnimationEnd(animation: Animator) {
                dispatchAddFinished(holder)
                activeAdds.remove(holder)
                dispatchAnimationsFinishedIfNoneIsRunning()
            }
        })

        pendingAdds[holder] = addAnimator

        return true
    }

    override fun animateMove(holder: RecyclerView.ViewHolder, fromX: Int, fromY: Int, toX: Int, toY: Int): Boolean {
        val deltaX = toX - fromX - holder.itemView.translationX
        val deltaY = toY - fromY - holder.itemView.translationY

        val moveAnimator = setupHolderAndCreateMoveAnimator(holder, deltaX, deltaY)

        moveAnimator.addListener(object : AnimatorListenerAdapter() {

            override fun onAnimationStart(animation: Animator) {
                val animator = pendingMoves.remove(holder)
                dispatchMoveStarting(holder)
                if (animator != null) {
                    activeMoves[holder] = animator
                }
            }

            override fun onAnimationEnd(animation: Animator) {
                dispatchMoveFinished(holder)
                activeMoves.remove(holder)
                dispatchAnimationsFinishedIfNoneIsRunning()
            }
        })

        pendingMoves[holder] = moveAnimator

        return true
    }

    override fun animateChange(
            oldHolder: RecyclerView.ViewHolder,
            newHolder: RecyclerView.ViewHolder,
            preInfo: ItemHolderInfo,
            postInfo: ItemHolderInfo
    ): Boolean {
        if (oldHolder !is TaxiListAdapter.ItemViewHolder
                || newHolder !is TaxiListAdapter.ItemViewHolder
                || oldHolder != newHolder) {
            return super.animateChange(oldHolder, newHolder, preInfo, postInfo)
        }

        val holder = newHolder

        val changeAnimator = setupItemViewHolderAndCreateChangeAnimator(
                holder,
                preInfo as TaxiListItemHolderInfo,
                postInfo as TaxiListItemHolderInfo)

        changeAnimator.addListener(object : AnimatorListenerAdapter() {

            override fun onAnimationStart(animation: Animator) {
                val animator = pendingChanges.remove(holder)
                dispatchAnimationStarted(holder)
                if (animator != null) {
                    activeChanges[holder] = animator
                }
            }

            override fun onAnimationEnd(animation: Animator) {
                activeChanges.remove(holder)
                dispatchAnimationFinished(holder)
                dispatchAnimationsFinishedIfNoneIsRunning()
            }
        })

        pendingChanges[holder] = changeAnimator

        return true
    }
}

class TaxiListItemHolderInfo : RecyclerView.ItemAnimator.ItemHolderInfo() {

    var payloads: MutableList<Any> = mutableListOf()
}