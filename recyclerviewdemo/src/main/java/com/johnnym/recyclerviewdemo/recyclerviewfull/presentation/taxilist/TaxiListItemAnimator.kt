package com.johnnym.recyclerviewdemo.recyclerviewfull.presentation.taxilist

import android.animation.*
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.RecyclerView
import android.animation.AnimatorSet

class TaxiListItemAnimator : DefaultItemAnimator() {

    private val viewHolderAnimatorMap: MutableMap<RecyclerView.ViewHolder, Animator> = HashMap()

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
        return super.isRunning() || viewHolderAnimatorMap.isNotEmpty()
    }

    override fun endAnimation(item: RecyclerView.ViewHolder) {
        viewHolderAnimatorMap[item]?.end()
        super.endAnimation(item)
    }

    override fun endAnimations() {
        viewHolderAnimatorMap.values.forEach { it.end() }
        super.endAnimations()
    }

    override fun animateRemove(holder: RecyclerView.ViewHolder?): Boolean {
        val removeAnimator = createTaxiListItemRemoveAnimator(holder as TaxiListAdapter.ItemViewHolder)

        removeAnimator.addListener(object : AnimatorListenerAdapter() {

            override fun onAnimationStart(animation: Animator) {
                viewHolderAnimatorMap.put(holder, animation)
            }

            override fun onAnimationEnd(animation: Animator) {
                dispatchRemoveFinished(holder)
                viewHolderAnimatorMap.remove(holder)
            }
        })

        removeAnimator.start()

        return false
    }

    override fun animateChange(
            oldHolder: RecyclerView.ViewHolder,
            newHolder: RecyclerView.ViewHolder,
            preInfo: ItemHolderInfo,
            postInfo: ItemHolderInfo
    ): Boolean {
        val viewHolder = newHolder as TaxiListAdapter.ItemViewHolder
        val oldInfo = preInfo as TaxiListItemHolderInfo
        val newInfo = postInfo as TaxiListItemHolderInfo

        val animatorNullableList: MutableList<Animator?> = mutableListOf()

        val itemPayload = createCombinedTaxiListItemPayload(oldInfo.payloads)

        val taxiStatusChange = itemPayload.taxiStatusChange
        if (taxiStatusChange != null && taxiStatusChange.old != taxiStatusChange.new) {
            animatorNullableList.add(createTaxiStatusChangeAnimator(viewHolder, itemPayload.taxiStatusChange))
        }

        val distanceChange = itemPayload.distanceChange
        if (distanceChange != null && distanceChange.old != distanceChange.new) {
            animatorNullableList.add(createDistanceChangeAnimator(viewHolder, itemPayload.distanceChange))
        }

        if (oldInfo.left != newInfo.right || oldInfo.top != newInfo.top) {
            animatorNullableList.add(createMoveChangeAnimator(viewHolder, oldInfo.left, oldInfo.top, newInfo.left, newInfo.top))
        }

        val animatorList = animatorNullableList.filterNotNull()
        val changeAnimator = AnimatorSet()
        changeAnimator.playTogether(animatorList)

        changeAnimator.addListener(object : AnimatorListenerAdapter() {

            override fun onAnimationStart(animation: Animator) {
                viewHolderAnimatorMap.put(viewHolder, animation)
                dispatchAnimationStarted(viewHolder)
            }

            override fun onAnimationEnd(animation: Animator) {
                dispatchAnimationFinished(viewHolder)
                viewHolderAnimatorMap.remove(viewHolder)
            }
        })

        changeAnimator.start()

        return false
    }
}