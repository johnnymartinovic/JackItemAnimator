package com.johnnym.recyclerviewdemo.recyclerviewfull.presentation.taxilist

import android.animation.*
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.RecyclerView
import android.animation.AnimatorSet
import android.support.annotation.ColorInt
import com.johnnym.recyclerviewdemo.recyclerviewfull.domain.TaxiStatus
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

    override fun runPendingAnimations() {
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

    override fun animateRemove(holder: RecyclerView.ViewHolder?): Boolean {
        val removeAnimator = createTaxiListItemRemoveAnimator(holder as TaxiListAdapter.ItemViewHolder)

        removeAnimator.addListener(object : AnimatorListenerAdapter() {

            override fun onAnimationStart(animation: Animator) {
                pendingRemoves.remove(holder)
                activeRemoves.put(holder, animation)
                dispatchRemoveStarting(holder)
            }

            override fun onAnimationEnd(animation: Animator) {
                dispatchRemoveFinished(holder)
                activeRemoves.remove(holder)
            }
        })

        pendingRemoves.put(holder, removeAnimator)

        return true
    }

    override fun animateAdd(holder: RecyclerView.ViewHolder): Boolean {
        holder.itemView.alpha = 0f

        val addAnimator = createTaxiListItemAddAnimator(holder as TaxiListAdapter.ItemViewHolder)

        addAnimator.addListener(object : AnimatorListenerAdapter() {

            override fun onAnimationStart(animation: Animator) {
                pendingAdds.remove(holder)
                activeAdds.put(holder, animation)
                dispatchAddStarting(holder)
            }

            override fun onAnimationEnd(animation: Animator) {
                dispatchAddFinished(holder)
                activeAdds.remove(holder)
            }
        })

        pendingAdds.put(holder, addAnimator)

        return true
    }

    override fun animateMove(holder: RecyclerView.ViewHolder, fromX: Int, fromY: Int, toX: Int, toY: Int): Boolean {
        val view = holder.itemView

        val deltaX = toX - fromX - view.translationX
        val deltaY = toY - fromY - view.translationY
        view.translationX = -deltaX
        view.translationY = -deltaY

        val moveAnimator = createMoveAnimator(holder as TaxiListAdapter.ItemViewHolder, deltaX, deltaY)

        moveAnimator.addListener(object : AnimatorListenerAdapter() {

            override fun onAnimationStart(animation: Animator) {
                pendingMoves.remove(holder)
                activeMoves.put(holder, animation)
                dispatchMoveStarting(holder)
            }

            override fun onAnimationEnd(animation: Animator) {
                dispatchMoveFinished(holder)
                activeMoves.remove(holder)
            }
        })

        pendingMoves.put(holder, moveAnimator)

        return true
    }

    override fun animateChange(
            oldHolder: RecyclerView.ViewHolder,
            newHolder: RecyclerView.ViewHolder,
            preInfo: ItemHolderInfo,
            postInfo: ItemHolderInfo
    ): Boolean {
        val viewHolder = newHolder as TaxiListAdapter.ItemViewHolder
        val view = viewHolder.itemView

        val oldInfo = preInfo as TaxiListItemHolderInfo
        val newInfo = postInfo as TaxiListItemHolderInfo

        val animatorNullableList: MutableList<Animator?> = mutableListOf()

        val itemPayload = createCombinedTaxiListItemPayload(oldInfo.payloads)

        val taxiStatusChange = itemPayload.taxiStatusChange
        if (taxiStatusChange != null && taxiStatusChange.old != taxiStatusChange.new) {
            @ColorInt val startColor: Int
            @ColorInt val endColor: Int
            if (taxiStatusChange.old == TaxiStatus.AVAILABLE) {
                startColor = viewHolder.statusAvailableColor
                endColor = viewHolder.statusUnavailableColor
            } else {
                startColor = viewHolder.statusUnavailableColor
                endColor = viewHolder.statusAvailableColor
            }

            viewHolder.statusBar.setBackgroundColor(startColor)

            animatorNullableList.add(createTaxiStatusChangeAnimator(viewHolder, startColor, endColor))
        }

        val distanceChange = itemPayload.distanceChange
        if (distanceChange != null && distanceChange.old != distanceChange.new) {
            animatorNullableList.add(createDistanceChangeAnimator(viewHolder, itemPayload.distanceChange))
        }

        if (oldInfo.left != newInfo.left || oldInfo.top != newInfo.top) {
            val deltaX = newInfo.left - oldInfo.left - view.translationX
            val deltaY = newInfo.top - oldInfo.top - view.translationY
            view.translationX = -deltaX
            view.translationY = -deltaY

            animatorNullableList.add(createMoveAnimator(viewHolder, deltaX, deltaY))
        }

        val animatorList = animatorNullableList.filterNotNull()
        val changeAnimator = AnimatorSet()
        changeAnimator.playTogether(animatorList)

        changeAnimator.addListener(object : AnimatorListenerAdapter() {

            override fun onAnimationStart(animation: Animator) {
                pendingChanges.remove(viewHolder)
                activeChanges.put(viewHolder, animation)
                dispatchAnimationStarted(viewHolder)
            }

            override fun onAnimationEnd(animation: Animator) {
                dispatchAnimationFinished(viewHolder)
                activeChanges.remove(viewHolder)
            }
        })

        pendingChanges.put(viewHolder, changeAnimator)

        return true
    }
}