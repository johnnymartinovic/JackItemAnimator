package com.johnnym.recyclerviewdemo.recyclerviewfull.presentation.taxilist

import android.animation.*
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.RecyclerView
import android.animation.AnimatorSet
import java.util.concurrent.ConcurrentHashMap

class TaxiListItemAnimator : DefaultItemAnimator() {

    private val pendingRemoves: MutableMap<RecyclerView.ViewHolder, Animator> = ConcurrentHashMap()
    private val pendingAdds: MutableMap<RecyclerView.ViewHolder, Animator> = ConcurrentHashMap()
    private val pendingMoves: MutableMap<RecyclerView.ViewHolder, Animator> = ConcurrentHashMap()
    private val pendingChanges: MutableMap<RecyclerView.ViewHolder, Animator> = ConcurrentHashMap()

    private val activeRemoves: MutableMap<RecyclerView.ViewHolder, Animator> = ConcurrentHashMap()
    private val activeAdds: MutableMap<RecyclerView.ViewHolder, Animator> = ConcurrentHashMap()
    private val activeMoves: MutableMap<RecyclerView.ViewHolder, Animator> = ConcurrentHashMap()
    private val activeChanges: MutableMap<RecyclerView.ViewHolder, Animator> = ConcurrentHashMap()

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
        pendingRemoves.remove(item)
        pendingAdds.remove(item)
        pendingMoves.remove(item)
        pendingChanges.remove(item)
        activeRemoves[item]?.end()
        activeAdds[item]?.end()
        activeMoves[item]?.end()
        activeChanges[item]?.end()
        super.endAnimation(item)
    }

    override fun endAnimations() {
        pendingRemoves.clear()
        pendingAdds.clear()
        pendingMoves.clear()
        pendingChanges.clear()
        activeRemoves.values.forEach { it.end() }
        activeAdds.values.forEach { it.end() }
        activeMoves.values.forEach { it.end() }
        activeChanges.values.forEach { it.end() }
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
                pendingRemoves.remove(holder)
                activeRemoves[holder] = animation
                dispatchRemoveStarting(holder)
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
                pendingAdds.remove(holder)
                activeAdds[holder] = animation
                dispatchAddStarting(holder)
            }

            override fun onAnimationEnd(animation: Animator) {
                dispatchAddFinished(holder)
                activeAdds.remove(holder)
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
                pendingMoves.remove(holder)
                activeMoves[holder] = animation
                dispatchMoveStarting(holder)
            }

            override fun onAnimationEnd(animation: Animator) {
                dispatchMoveFinished(holder)
                activeMoves.remove(holder)
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
                pendingChanges.remove(holder)
                activeChanges[holder] = animation
                dispatchAnimationStarted(holder)
            }

            override fun onAnimationEnd(animation: Animator) {
                dispatchAnimationFinished(holder)
                activeChanges.remove(holder)
            }
        })

        pendingChanges[holder] = changeAnimator

        return true
    }
}

class TaxiListItemHolderInfo : RecyclerView.ItemAnimator.ItemHolderInfo() {

    var payloads: MutableList<Any> = mutableListOf()
}